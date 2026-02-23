package com.ibosng.usercreationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class DienstortDto {
    private String bundesland;
    private String plz;
    private String stadt;
    private String anschrift;
}
