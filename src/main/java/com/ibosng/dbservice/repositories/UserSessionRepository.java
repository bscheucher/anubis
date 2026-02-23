package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional("postgresTransactionManager")
public interface UserSessionRepository extends JpaRepository<UserSession, Integer> {

    Optional<UserSession> getByToken(String token);

//    Optional<UserSession> getByTokenAndBenutzer(String token, Benutzer benutzer);

    List<UserSession> getAllByBenutzerAndActive(Benutzer benutzer, boolean isActive);

//    List<UserSession> getByBenutzerAndActive(Integer benutzerId, boolean isActive);
}
