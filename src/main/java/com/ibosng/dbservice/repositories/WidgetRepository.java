package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.Widget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface WidgetRepository extends JpaRepository<Widget, Integer> {

}
