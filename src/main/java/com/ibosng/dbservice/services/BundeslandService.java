package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.Bundesland;

public interface BundeslandService extends BaseService<Bundesland> {
    Bundesland findByPlzId(Integer plzId);
}
