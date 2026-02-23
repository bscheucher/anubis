package com.ibosng.dbibosservice.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class BasicPersonDto implements Serializable {
    private Long id;
    private String name;
}
