package com.ibosng.dbservice.repositories;
import com.ibosng.dbservice.entities.ZusatzInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZusatzInfoRepository extends JpaRepository<ZusatzInfo, Integer> {
}
