package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.UserSession;

import java.util.List;

public interface UserSessionService extends BaseService<UserSession> {
    UserSession getSessionByToken(String token);

    // TODO fix this
    List<UserSession> getUserSessions(String token, boolean isActive);

    boolean killAllUserSessions(String token);

    boolean killActiveSession(String token);
}
