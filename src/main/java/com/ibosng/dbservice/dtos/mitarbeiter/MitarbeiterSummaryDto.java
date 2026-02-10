package com.ibosng.dbservice.dtos.mitarbeiter;

import com.ibosng.dbservice.dtos.workflows.WorkflowItemDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MitarbeiterSummaryDto {
    private String vorname;
    private String nachname;
    private String svnr;
    private String personalnummer;
    private LocalDate eintritt;
    private String kostenstelle;
    private WorkflowItemDto workflowItem;
}
