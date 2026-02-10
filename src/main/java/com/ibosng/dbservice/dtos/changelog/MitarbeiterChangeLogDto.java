package com.ibosng.dbservice.dtos.changelog;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MitarbeiterChangeLogDto {
    private List<ChangeLogDto> vertragsaenderungen = new ArrayList<>();
    private List<ChangeLogDto> stammdatenAenderungen = new ArrayList<>();
}
