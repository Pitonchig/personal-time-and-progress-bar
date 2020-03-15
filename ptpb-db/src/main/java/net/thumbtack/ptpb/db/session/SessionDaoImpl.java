package net.thumbtack.ptpb.db.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SessionDaoImpl implements SessionDao {
    private SessionMapper sessionMapper;

    @Autowired
    public SessionDaoImpl(SessionMapper sessionMapper) {
        this.sessionMapper = sessionMapper;
    }
}
