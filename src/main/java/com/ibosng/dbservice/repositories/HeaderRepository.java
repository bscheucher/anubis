package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.FileImportHeaders;
import com.ibosng.dbservice.entities.FileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional("postgresTransactionManager")
public interface HeaderRepository extends JpaRepository<FileImportHeaders, Integer> {

    Optional<FileImportHeaders> findByFileType(FileType fileType);

}