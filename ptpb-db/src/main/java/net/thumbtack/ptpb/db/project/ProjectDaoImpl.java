package net.thumbtack.ptpb.db.project;

import org.springframework.beans.factory.annotation.Autowired;

public class ProjectDaoImpl implements ProjectDao {
    private ProjectMapper projectMapper;

    @Autowired
    public ProjectDaoImpl(ProjectMapper projectMapper) {
        this.projectMapper = projectMapper;
    }
}
