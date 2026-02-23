package com.ibosng.gatewayservice.dtos.teilnehmerPayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MassnahmenummerPayload {
    private String massnahmenummer;
    private List<Integer> teilnehmer;

}
