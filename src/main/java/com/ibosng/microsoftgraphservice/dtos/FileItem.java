package com.ibosng.microsoftgraphservice.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FileItem extends TreeNode {
    public FileItem() {
        super();
        this.setContent(null); // Explicitly set content to null for files
    }
}
