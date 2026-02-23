package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.IbosReference;

import java.util.List;

public interface IbosReferenceService extends BaseService<IbosReference> {
    List<IbosReference> findAllByIbosId(Integer ibosId);

    List<IbosReference> findAllByIbosngId(Integer ibosngId);

    List<IbosReference> findAllByIbosIdAndData(Integer ibosId, String data);

    List<IbosReference> findAllByIbosngIdAndData(Integer ibosngId, String data);

    List<IbosReference> findAllByData(String data);

    List<IbosReference> findAllByDataStartingWith(String data);
}
