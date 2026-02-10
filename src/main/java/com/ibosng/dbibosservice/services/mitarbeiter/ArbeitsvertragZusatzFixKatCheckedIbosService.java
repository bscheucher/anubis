package com.ibosng.dbibosservice.services.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.ArbeitsvertragZusatzFixKatCheckedIbos;
import com.ibosng.dbibosservice.services.BaseService;

import java.util.List;

public interface ArbeitsvertragZusatzFixKatCheckedIbosService extends BaseService<ArbeitsvertragZusatzFixKatCheckedIbos> {
    List<ArbeitsvertragZusatzFixKatCheckedIbos> findAllByIdArbeitsvertragZusatzId(Integer arbeitsvertragZusatzId);

}
