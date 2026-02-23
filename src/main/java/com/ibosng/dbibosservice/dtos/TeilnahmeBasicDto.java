package com.ibosng.dbibosservice.dtos;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TeilnahmeBasicDto {
    private List<Integer> seminars = new ArrayList();
    private Integer teilnehmerNumber;

}
