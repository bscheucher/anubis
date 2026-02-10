package com.ibosng.dbservice.dtos.changelog;

import com.ibosng.dbservice.dtos.mitarbeiter.vertragsaenderung.VertragsaenderungDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VertragsaenderungChangeLogDto {
    private List<VertragsaenderungDto> vertragsaenderungen = new ArrayList<>();
}
