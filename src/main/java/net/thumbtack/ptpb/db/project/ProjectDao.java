package net.thumbtack.ptpb.db.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProjectDao {

    private final ProjectMapper projectMapper;

    public void insertProject(Project project) {
        projectMapper.save(project);
    }

    public Optional<Project> getProjectById(String id) {
        return projectMapper.findById(id);
    }

    public List<Project> getProjectsByUserId(String userId) {
        List<Project> projects = new LinkedList<>();
        projectMapper.findByUserId(userId).forEach(projects::add);
        return projects;
    }

    public void updateProject(Project project) {
        projectMapper.save(project);
    }

    public void deleteProject(String projectId) {
        projectMapper.deleteById(projectId);
    }
}
