package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.Land;

import java.util.List;

public interface LandService extends BaseService<Land>{
    List<Land> findByTelefonvorwahl(String vorwahl);
    Land findByEldaCode(String vorwahl);
    Land findByLandName(String landname);
    Land getLandFromCountryCode(String countryCode);
    List<Land> findByIsInEuEeaCh(Boolean isIn);
    List<Land> findByLandCode(String landcode);
    Land findAllByLhrKz(String lhrKz);
}
