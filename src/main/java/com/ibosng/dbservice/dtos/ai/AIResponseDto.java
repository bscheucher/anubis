package com.ibosng.dbservice.dtos.ai;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AIResponseDto {
    private String threadId;
    private String request;
    private String response;
}
