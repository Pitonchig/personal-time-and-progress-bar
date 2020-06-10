package net.thumbtack.ptpb.db.session;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SessionDao {
    private final SessionMapper sessionMapper;

    public void insert(Session session) {
        sessionMapper.save(session);
    }

    public Optional<Session> getSessionByUuid(String uuid) {
        return sessionMapper.findById(uuid);
    }

    public boolean isOnline(String uuid) {
        return sessionMapper.existsById(uuid);
    }

    public List<Session> getAllSessions() {
        List<Session> sessions = new LinkedList<>();
        sessionMapper.findAll().forEach(sessions::add);
        return sessions;
    }

    public void deleteAllSessions() {
        sessionMapper.deleteAll();
    }

    public void update(Session session) {
        sessionMapper.save(session);
    }
}
