package com.ibosng.microsoftgraphservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TreeNode {
    private String id;          // Unique identifier for the node
    private String title;       // Name or title of the file/folder
    private String path;
    private String createdAt;   // Timestamp of creation
    private String errorMessage;

    @JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null lists for files so that it gets ommited in response, but still shows empy lists inside folders.
    private List<TreeNode> content; // For folders only
    @JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null for folders due to them not having a mimetype.
    private String mimeType;    // MIME type (for files only)
}