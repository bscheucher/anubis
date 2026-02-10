package com.ibosng.dbservice.services.mitarbeiter;

import com.ibosng.dbservice.dtos.mitarbeiter.ZulageDto;
import com.ibosng.dbservice.entities.mitarbeiter.GehaltInfoZulage;
import com.ibosng.dbservice.services.BaseService;

import java.util.List;
import java.util.Optional;

public interface GehaltInfoZulageService extends BaseService<GehaltInfoZulage> {

    List<GehaltInfoZulage> findAllByGehaltInfoId(Integer gehaltInfoId);

    ZulageDto mapGehaltInfoZulageToDto(GehaltInfoZulage gehaltInfoZulage);

    Optional<GehaltInfoZulage> findByArtDerZulageAndGehaltInfoId(String artDerZulage, Integer gehaltInfoId);

    void deleteByArtDerZulageAndGehaltInfoId(String artDerZulage, Integer gehaltInfoId);

}
