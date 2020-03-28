package net.thumbtack.ptpb.db.session;

import java.util.List;
import java.util.Optional;

public interface SessionDao {

    void insert(Session session);

    Optional<Session> getSessionByUuid(String uuid);

    boolean isOnline(String uuid);

    List<Session> getAllSessions();

    void deleteAllSessions();
}
