package net.thumbtack.ptpb.db.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProjectDaoImpl implements ProjectDao {
    private final ProjectMapper projectMapper;
}
