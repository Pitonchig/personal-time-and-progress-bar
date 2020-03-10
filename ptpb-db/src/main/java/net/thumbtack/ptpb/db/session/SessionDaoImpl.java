package net.thumbtack.ptpb.db.session;

import org.springframework.beans.factory.annotation.Autowired;

public class SessionDaoImpl implements SessionDao {
    private SessionMapper sessionMapper;

    @Autowired
    public SessionDaoImpl(SessionMapper sessionMapper) {
        this.sessionMapper = sessionMapper;
    }
}
