package com.ibosng.dbservice.dtos.mitarbeiter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MAFilteredResultDto {
    private String personalnummer;
    private String name;
    private String kostenstelle;
    private String fuehrungskraft;
    private String svnr;
    private String nachname;
}
