package com.ibosng.lhrservice.dtos.variabledaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibosng.lhrservice.dtos.GruppeNameKzDto;
import lombok.Data;

@Data
@JsonInclude()
public class LohnsteuerDto {
    private GruppeNameKzDto dienstnehmerart;
    private FreibetraegeDto freibetraege;
    private GruppeNameKzDto gruppe;
    private GruppeNameKzDto jahresausgleich;
    private PendlerpauschaleDto pendlerpauschale;
    private Boolean ueberwiegendNacht;
    private Boolean teilzeit;

}
