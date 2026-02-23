package com.ibosng.gatewayservice.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayloadResponse implements Serializable {

    private boolean success;
    private List<PayloadType> data;
    private Pagination pagination;
    private String message;
}
