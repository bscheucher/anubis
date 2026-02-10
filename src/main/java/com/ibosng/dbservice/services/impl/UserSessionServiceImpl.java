package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.UserSession;
import com.ibosng.dbservice.repositories.UserSessionRepository;
import com.ibosng.dbservice.services.UserSessionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserSessionServiceImpl implements UserSessionService {

    private final UserSessionRepository userSessionRepository;

    public UserSessionServiceImpl(UserSessionRepository userSessionRepository) {
        this.userSessionRepository = userSessionRepository;
    }


    @Override
    public List<UserSession> findAll() {
        return userSessionRepository.findAll();
    }

    @Override
    public Optional<UserSession> findById(Integer id) {
        return userSessionRepository.findById(id);
    }

    @Override
    public UserSession save(UserSession object) {
        return userSessionRepository.save(object);
    }

    @Override
    public List<UserSession> saveAll(List<UserSession> objects) {
        return userSessionRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        userSessionRepository.deleteById(id);
    }

    @Override
    public List<UserSession> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public UserSession getSessionByToken(String token) {
        Optional<UserSession> userSession = userSessionRepository.getByToken(token);
        if(userSession.isPresent()){
            return userSession.get();
        }
        return null;
    }

    // TODO fix this
    @Override
    public List<UserSession> getUserSessions(String token, boolean isActive) {
        Optional<UserSession> userSession = userSessionRepository.getByToken(token);
        Integer benutzerId = null;
        if(userSession.isPresent()){
            benutzerId = userSession.get().getBenutzer().getId();
        }
        if(userSession.isPresent()) {
            Benutzer benutzer = new Benutzer();
            benutzer.setId(benutzerId);
            return userSessionRepository.getAllByBenutzerAndActive(benutzer,isActive);
        }
        return new ArrayList<UserSession>();
    }

    @Override
    public boolean killAllUserSessions(String token){
        List<UserSession> userActiveSessions = getUserSessions(token,true);
        for (UserSession userActiveSession : userActiveSessions){
            userActiveSession.setInvalidatedOn(LocalDateTime.now());
            userActiveSession.setActive(false);
        }
        userSessionRepository.saveAll(userActiveSessions);
        if(getUserSessions(token,true).isEmpty()){
            return true;
        }
        return false;
    }

    @Override
    public boolean killActiveSession(String token){
        UserSession activeSession = getSessionByToken(token);
        if(activeSession != null){
            activeSession.setInvalidatedOn(LocalDateTime.now());
            activeSession.setActive(false);
            UserSession invalidatedSession = userSessionRepository.save(activeSession);
            if(!invalidatedSession.getActive()){
                return true;
            }
        }
        return false;
    }
}
