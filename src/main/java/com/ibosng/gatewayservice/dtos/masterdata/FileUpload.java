package com.ibosng.gatewayservice.dtos.masterdata;

import com.ibosng.dbservice.entities.mitarbeiter.BlobStatus;
import com.ibosng.gatewayservice.enums.FileUploadTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUpload {

    private String identifier;
    private String additionalIdentifier;
    private BlobStatus status;
    private FileUploadTypes type;
}
