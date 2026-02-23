package com.ibosng.lhrservice.dtos.kostenstellenaufteilung;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

import static com.ibosng.lhrservice.utils.Constants.LOCAL_DATE_FORMAT;

@Data
@JsonInclude()
public class KostenstellenaufteilungDto {
    private List<KostenstellenaufteilungDataDto> data;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = LOCAL_DATE_FORMAT)
    private LocalDate validFrom;
}
