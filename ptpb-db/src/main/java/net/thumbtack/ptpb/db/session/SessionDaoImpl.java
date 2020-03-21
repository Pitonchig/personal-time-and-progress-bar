package net.thumbtack.ptpb.db.session;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SessionDaoImpl implements SessionDao {
    private final SessionMapper sessionMapper;
}
