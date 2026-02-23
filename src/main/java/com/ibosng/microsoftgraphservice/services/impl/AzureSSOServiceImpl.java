package com.ibosng.microsoftgraphservice.services.impl;


import com.ibosng.microsoftgraphservice.services.AzureSSOService;
import com.microsoft.graph.models.DirectoryObject;
import com.microsoft.graph.models.Group;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.DirectoryObjectCollectionWithReferencesPage;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.GroupCollectionPage;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class AzureSSOServiceImpl implements AzureSSOService {

    private final GraphServiceClient<Request> graphClient;

    public AzureSSOServiceImpl(@Qualifier("ssoGraphClient") GraphServiceClient<Request> graphClient) {
        this.graphClient = graphClient;
    }

    /**
     * Fetches all groups (including nested ones) that a user belongs to.
     */
    @Override
    public List<String> getNestedGroups(String userId) {
        Set<String> groups = new HashSet<>();
        DirectoryObjectCollectionWithReferencesPage currentPage = graphClient
                .users(userId)
                .transitiveMemberOf()
                .buildRequest()
                .get();

        // Loop through all pages
        while (currentPage != null) {
            currentPage.getCurrentPage().stream()
                    .filter(obj -> obj instanceof Group)
                    .map(obj -> ((Group) obj).displayName)
                    .forEach(groups::add);

            currentPage = currentPage.getNextPage() != null
                    ? currentPage.getNextPage().buildRequest().get()
                    : null;
        }
        return groups.stream().filter(group -> group.startsWith("FN_")).toList();
    }

    @Override
    public List<String> getGroupMemberEmailsByName(String groupName) {
        String groupId = getGroupIdByName(groupName);

        if (groupId == null) {
            log.error("Group not found for {}", groupName);
            return Collections.emptyList();
        }

        Set<String> visitedGroups = new HashSet<>();
        Set<String> collectedEmails = new HashSet<>();

        collectGroupMemberEmailsRecursive(groupId, visitedGroups, collectedEmails);

        return new ArrayList<>(collectedEmails);
    }

    private void collectGroupMemberEmailsRecursive(String groupId, Set<String> visitedGroups, Set<String> collectedEmails) {
        if (!visitedGroups.add(groupId)) {
            // Already visited this group, avoid infinite recursion
            return;
        }

        DirectoryObjectCollectionWithReferencesPage currentPage = graphClient
                .groups(groupId)
                .members()
                .buildRequest()
                .get();

        while (currentPage != null) {
            for (DirectoryObject member : currentPage.getCurrentPage()) {
                if (member instanceof User) {
                    String email = ((User) member).mail;
                    if (email != null && !email.isEmpty()) {
                        collectedEmails.add(email);
                    }
                } else if (member instanceof Group) {
                    String nestedGroupId = ((Group) member).id;
                    collectGroupMemberEmailsRecursive(nestedGroupId, visitedGroups, collectedEmails);
                }
            }

            currentPage = currentPage.getNextPage() != null
                    ? currentPage.getNextPage().buildRequest().get()
                    : null;
        }
    }

    /**
     * Fetches the group ID given a group name.
     */
    private String getGroupIdByName(String groupName) {
        GroupCollectionPage groupsPage = graphClient.groups()
                .buildRequest()
                .filter("displayName eq '" + groupName + "'")
                .get();

        if (groupsPage != null && !groupsPage.getCurrentPage().isEmpty()) {
            return groupsPage.getCurrentPage().get(0).id; // Return the first matching group's ID
        }
        return null; // Group not found
    }

    @Override
    public String getUserUpnById(String userId) {
        try {
            User user = graphClient
                    .users(userId)
                    .buildRequest()
                    .select("userPrincipalName")
                    .get();

            return user.userPrincipalName;
        } catch (Exception ex) {
            log.error("Failed to fetch UPN for userId {}: {}", userId, ex.getMessage());
            return null;
        }
    }
}
