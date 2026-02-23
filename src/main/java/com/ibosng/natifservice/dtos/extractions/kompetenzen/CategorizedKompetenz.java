package com.ibosng.natifservice.dtos.extractions.kompetenzen;

import com.ibosng.natifservice.dtos.extractions.DetailsDto;
import lombok.Data;

@Data
public class CategorizedKompetenz {
    
    private DetailsDto kompetenz;
    private DetailsDto score;
    private String art;

}