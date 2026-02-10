package com.ibosng.gatewayservice.services;

import com.ibosng.gatewayservice.dtos.response.PayloadResponse;

public interface FolderStructureService {

    PayloadResponse fetchFolderStructure(String identifier);

    PayloadResponse fetchOwnFolderStructure(String token);
}
