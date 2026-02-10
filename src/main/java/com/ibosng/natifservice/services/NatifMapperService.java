package com.ibosng.natifservice.services;

import com.ibosng.natifservice.dtos.extractions.ExtractionsDto;

public interface NatifMapperService {
    void saveKompetenz(ExtractionsDto extractionsDto, Integer teilnehmerId);
}
