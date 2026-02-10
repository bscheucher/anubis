package com.ibosng.dbibosservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrAdId implements Serializable {
    private Integer gruppeGrnr;
    private Integer adresseAdadnr;
}