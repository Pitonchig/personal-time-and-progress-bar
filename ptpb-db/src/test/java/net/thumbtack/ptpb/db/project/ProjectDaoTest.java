package net.thumbtack.ptpb.db.project;


import lombok.RequiredArgsConstructor;
import net.thumbtack.ptpb.db.DbConfiguration;
import net.thumbtack.ptpb.db.DbProperties;
import net.thumbtack.ptpb.db.item.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EnableConfigurationProperties
@ContextConfiguration(classes = {
        DbConfiguration.class,
        DbProperties.class,
        ProjectDaoImpl.class
})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectDaoTest {

    private final ProjectDao projectDao;

    @BeforeEach
    void setup() {
        projectDao.deleteAllProjects();
    }

    @Test
    void testInsertAndGetProjectById() {
        long projectId = System.nanoTime();
        Project project = Project.builder()
                .id(projectId)
                .projectName("project name")
                .userName("user")
                .isFavorited(false)
                .color(39)
                .build();
        projectDao.insertProject(project);

        Optional<Project> result = projectDao.getProjectById(projectId);
        assertTrue(result.isPresent());
        assertEquals(project, result.get());
    }

    @Test
    void testGetAllProjects() {
        List<Project> projects = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            long projectId = System.nanoTime();
            Project project = Project.builder()
                    .id(projectId)
                    .projectName("project name")
                    .userName("user")
                    .isFavorited(false)
                    .color(39)
                    .build();
            projects.add(project);
        }
        projects.forEach(projectDao::insertProject);

        Project notInsertedProject = Project.builder()
                .id(System.nanoTime())
                .projectName("not inserted project name")
                .userName("user")
                .isFavorited(false)
                .color(39)
                .build();

        List<Project> results = projectDao.getAllProjects();
        assertAll(
                () -> assertTrue(projects.containsAll(results)),
                () -> assertEquals(projects.size(), results.size()),
                () -> assertFalse(results.contains(notInsertedProject))
        );
    }

    @Test
    void testDeleteAllProjects() {
        int count = 10;
        assertEquals(0, projectDao.getAllProjects().size());

        for (int i = 0; i < count; i++) {
            long projectId = System.nanoTime();
            Project project = Project.builder()
                    .id(projectId)
                    .projectName(String.format("not inserted project name %d", i))
                    .userName("user")
                    .isFavorited(false)
                    .color(39)
                    .build();
            projectDao.insertProject(project);
        }
        assertEquals(count, projectDao.getAllProjects().size());
        projectDao.deleteAllProjects();
        assertEquals(0, projectDao.getAllProjects().size());
    }

}
