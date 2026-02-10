package com.ibosng.lhrservice.dtos.variabledaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude()
public class KollektivvertragEinstufungDto {
    private String gruppe;
    private String klasse;
    private String stufe;
}
