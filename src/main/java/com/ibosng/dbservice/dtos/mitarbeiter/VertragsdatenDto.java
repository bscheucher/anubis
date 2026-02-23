package com.ibosng.dbservice.dtos.mitarbeiter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VertragsdatenDto extends VertragsdatenBasicDto {
    private Integer id;
    private String personalnummer;
    private String kostenstelle;

    private String strasse;
    private String land;
    private String plz;
    private String ort;

    private List<VordienstzeitenDto> vordienstzeiten;
    private List<UnterhaltsberechtigteDto> unterhaltsberechtigte;

    private List<String> errors = new ArrayList<>();
    private Map<String, String> errorsMap = new HashMap<>();

    private String dienstnehmergruppe;
    private String abrechnungsgruppe;
    private boolean isKernzeit;

    private Integer lehrjahr;

    private String klasse;
}