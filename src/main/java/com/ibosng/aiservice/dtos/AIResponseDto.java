package com.ibosng.aiservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AIResponseDto {
    private String threadId;
    private String request;
    private String response;
}
