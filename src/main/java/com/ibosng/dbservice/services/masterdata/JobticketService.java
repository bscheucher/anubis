package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.masterdata.Jobticket;
import com.ibosng.dbservice.services.BaseService;

import java.util.List;
import java.util.Optional;

public interface JobticketService extends BaseService<Jobticket> {
    Optional<Jobticket> findByName(String name);

    List<Jobticket> findByStatus(Status status);
}
