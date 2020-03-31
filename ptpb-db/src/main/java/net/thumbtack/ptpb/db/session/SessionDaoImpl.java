package net.thumbtack.ptpb.db.session;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SessionDaoImpl implements SessionDao {
    private final SessionMapper sessionMapper;

    @Override
    public void insert(Session session) {
        sessionMapper.save(session);
    }

    @Override
    public Optional<Session> getSessionByUuid(String uuid) {
        return sessionMapper.findById(uuid);
    }

    @Override
    public boolean isOnline(String uuid) {
        return sessionMapper.existsById(uuid);
    }

    @Override
    public List<Session> getAllSessions() {
        List<Session> sessions = new LinkedList<>();
        sessionMapper.findAll().forEach(sessions::add);
        return sessions;
    }

    @Override
    public void deleteAllSessions() {
        sessionMapper.deleteAll();
    }

    @Override
    public void update(Session session) {
        sessionMapper.save(session);
    }
}
