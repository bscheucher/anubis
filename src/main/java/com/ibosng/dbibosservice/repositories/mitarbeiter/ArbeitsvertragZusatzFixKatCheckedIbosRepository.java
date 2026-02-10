package com.ibosng.dbibosservice.repositories.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.ArbeitsvertragZusatzFixKatCheckedIbos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("mariaDbTransactionManager")
public interface ArbeitsvertragZusatzFixKatCheckedIbosRepository extends JpaRepository<ArbeitsvertragZusatzFixKatCheckedIbos, Integer> {
    List<ArbeitsvertragZusatzFixKatCheckedIbos> findAllByIdArbeitsvertragZusatzId(Integer arbeitsvertragZusatzId);
}
