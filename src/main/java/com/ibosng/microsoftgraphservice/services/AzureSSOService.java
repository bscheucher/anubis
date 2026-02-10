package com.ibosng.microsoftgraphservice.services;

import java.util.List;

public interface AzureSSOService {
    List<String> getNestedGroups(String userId);

    List<String> getGroupMemberEmailsByName(String groupName);

    String getUserUpnById(String userId);
}
