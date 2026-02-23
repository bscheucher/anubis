package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.DataReferenceTemp;

import java.util.List;
import java.util.Map;

public interface DataReferenceTempService extends BaseService<DataReferenceTemp> {

    String REFERENCE = "reference";
    String DATA1 = "data1";
    String DATA2 = "data2";
    String DATA3 = "data3";
    String DATA4 = "data4";
    String DATA5 = "data5";
    String DATA6 = "data6";
    String DATA7 = "data7";
    String DATA8 = "data8";
    String DATA9 = "data9";
    String DATA10 = "data10";

    List<DataReferenceTemp> findAllByReference(String reference);

    List<DataReferenceTemp> search(Map<String, String> filters);
}
