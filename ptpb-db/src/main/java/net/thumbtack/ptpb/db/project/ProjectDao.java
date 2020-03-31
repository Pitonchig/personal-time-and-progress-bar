package net.thumbtack.ptpb.db.project;

import java.util.List;
import java.util.Optional;

public interface ProjectDao {
    Optional<Project> getProjectById(long id);

    List<Project> getAllProjects();

    void insertProject(Project project);

    void deleteAllProjects();

    List<Project> getProjectsByUserId(long userId);

    void deleteProjectById(long projectId);
}
