package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.betreuer.Betreuer;

public interface BetreuerService extends BaseService<Betreuer>{
    Betreuer findByVorname(String vorname);
    Betreuer findByNachname(String nachname);
    Betreuer findByVornameAndNachname(String vorname, String nachname);
}
