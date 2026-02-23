package com.ibosng.gatewayservice.dtos.ai;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerReplacementDto {
    private Integer id;
    private String trainerVorname;

    private String trainerNachname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String von;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String bis;

    public TrainerReplacementDto(String trainerVorname, String trainerNachname, String von, String bis) {
        this.trainerVorname = trainerVorname;
        this.trainerNachname = trainerNachname;
        this.von = von;
        this.bis = bis;
    }
}
