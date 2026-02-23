package com.ibosng.dbservice.dtos.mitarbeiter.vertragsaenderung;

import com.ibosng.dbservice.dtos.changelog.FieldChangeDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class VertragsaenderungDto {
    private Integer id;
    @NotNull(message = "Personal number is empty")
    private String personalnummer;
    private String antragssteller;
    private LocalDate gueltigAb;
    private LocalDateTime createdAt;
    private Integer successor;
    private Integer predecessor;
    private String interneAnmerkung;
    private String offizielleBemerkung;
    private String kommentar;
    private String status;
    private String genehmigender;
    private List<FieldChangeDto> fieldChanges = new ArrayList<>();
    private List<String> emailRecipients = new ArrayList<>();

    private List<String> errors = new ArrayList<>();
    private Map<String, String> errorsMap = new HashMap<>();
}
