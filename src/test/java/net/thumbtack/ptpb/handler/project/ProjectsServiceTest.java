package net.thumbtack.ptpb.handler.project;

import net.thumbtack.ptpb.db.session.Session;
import net.thumbtack.ptpb.db.session.SessionDao;
import net.thumbtack.ptpb.db.user.Item;
import net.thumbtack.ptpb.db.user.Project;
import net.thumbtack.ptpb.db.user.User;
import net.thumbtack.ptpb.db.user.UserDao;
import net.thumbtack.ptpb.handler.project.dto.ItemDto;
import net.thumbtack.ptpb.handler.project.dto.request.UpdateProjectRequest;
import net.thumbtack.ptpb.handler.project.dto.response.ProjectResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ProjectsServiceTest {

    private ProjectsService projectsService;
    private String sessionUuid;
    private Session session;

    @MockBean
    private SessionDao sessionDao;
    @MockBean
    private UserDao userDao;

    @BeforeEach
    void setup() {
        sessionUuid = UUID.randomUUID().toString();
        session = Session.builder()
                .uuid(sessionUuid)
                .userId(UUID.randomUUID().toString())
                .isExpired(false)
                .dateTime(ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
        projectsService = new ProjectsService(sessionDao, userDao);
        when(sessionDao.getSessionByUuid(sessionUuid)).thenReturn(Optional.of(session));
    }


    @Test
    void testGetUserProjects() throws Exception {
        Project project1 = Project.builder()
                .id(UUID.randomUUID().toString())
                .projectName("project1")
                .item(Item.builder().id(UUID.randomUUID().toString()).content("item1-1").build())
                .item(Item.builder().id(UUID.randomUUID().toString()).content("item1-2").build())
                .item(Item.builder().id(UUID.randomUUID().toString()).content("item1-3").build())
                .build();

        Project project2 = Project.builder()
                .id(UUID.randomUUID().toString())
                .projectName("project2")
                .item(Item.builder().id(UUID.randomUUID().toString()).content("item2-1").build())
                .item(Item.builder().id(UUID.randomUUID().toString()).content("item2-2").build())
                .build();

        User user = User.builder()
                .id(session.getUserId())
                .name("User")
                .project(project1)
                .project(project2)
                .build();

        when(userDao.getUserById(session.getUserId())).thenReturn(Optional.ofNullable(user));
        List<ProjectResponse> responses = projectsService.getUserProjects(sessionUuid);

        List<Project> projects = user.getProjects();
        assertNotNull(projects);
        assertEquals(projects.size(), responses.size());
        for (ProjectResponse response : responses) {
            Optional<Project> optional = projects.stream().filter(p -> response.getId().equals(p.getId())).findFirst();
            assertTrue(optional.isPresent());
            Project project = optional.get();

            assertEquals(project.getProjectName(), response.getName());
            List<String> responseItems = response.getItems().stream().map(ItemDto::getId).collect(Collectors.toList());
            List<String> projectItems = project.getItems().stream().map(Item::getId).collect(Collectors.toList());
            assertEquals(responseItems.size(), projectItems.size());
            assertTrue(responseItems.containsAll(projectItems));
        }
    }

    @Test
    void testUpdateUserProjects() throws Exception {
        User user = User.builder()
                .id(session.getUserId())
                .name("User")
                .isTodoistLinked(true)
                .build();
        when(userDao.getUserById(session.getUserId())).thenReturn(Optional.ofNullable(user));

        UpdateProjectRequest request1 = UpdateProjectRequest.builder()
                .id(UUID.randomUUID().toString())
                .name("project1")
                .item(ItemDto.builder().id(UUID.randomUUID().toString()).content("item1-1").build())
                .item(ItemDto.builder().id(UUID.randomUUID().toString()).content("item1-2").build())
                .build();

        UpdateProjectRequest request2 = UpdateProjectRequest.builder()
                .id(UUID.randomUUID().toString())
                .name("project2")
                .item(ItemDto.builder().id(UUID.randomUUID().toString()).content("item2-1").build())
                .item(ItemDto.builder().id(UUID.randomUUID().toString()).content("item2-2").build())
                .item(ItemDto.builder().id(UUID.randomUUID().toString()).content("item2-3").build())
                .build();
        List<UpdateProjectRequest> requests = Arrays.asList(request1, request2);

        List<ProjectResponse> responses = projectsService.updateUserProjects(requests, sessionUuid);
        assertNotNull(responses);
        assertEquals(requests.size(), responses.size());
        for (ProjectResponse response : responses) {
            Optional<UpdateProjectRequest> optional = requests.stream().filter(p -> response.getId().equals(p.getId())).findFirst();
            assertTrue(optional.isPresent());
            UpdateProjectRequest project = optional.get();

            assertEquals(project.getName(), response.getName());
            List<String> responseItems = response.getItems().stream().map(ItemDto::getId).collect(Collectors.toList());
            List<String> projectItems = project.getItems().stream().map(ItemDto::getId).collect(Collectors.toList());
            assertEquals(responseItems.size(), projectItems.size());
            assertTrue(responseItems.containsAll(projectItems));
        }
    }

    /*
    @Test
    void testCreateProject() throws PtpbException {
        CreateProjectRequest request = CreateProjectRequest.builder()
                .name("Test project")
                .color(31)
                .isFavorite(false)
                .build();
        CreateProjectResponse response = projectsService.createProject(request, uuid);
        ArgumentCaptor<Project> captor = ArgumentCaptor.forClass(Project.class);
        verify(projectDao, times(1)).insertProject(captor.capture());
        Project projectCaptured = captor.getValue();

        assertNotNull(response);
        assertNotNull(projectCaptured);
        assertEquals(request.getName(), projectCaptured.getProjectName());
        assertEquals(request.getColor(), projectCaptured.getColor());
        assertEquals(request.isFavorite(), projectCaptured.isFavorite());
        assertTrue(response.getId() > 0);
    }

    @Test
    void testGetProject() throws PtpbException {
        Project project = Project.builder()
                .id(System.nanoTime())
                .userId(session.getUserId())
                .color(34)
                .isFavorite(false)
                .projectName("project name")
                .build();
        when(projectDao.getProjectById(project.getId())).thenReturn(Optional.of(project));
        GetProjectResponse getProjectResponse = projectsService.getProject(project.getId(), uuid);
        assertNotNull(getProjectResponse);
        verify(itemDao, times(1)).getItemsByProjectId(project.getId());
    }

    @Test
    void testDeleteProject() throws PtpbException {
        Project project = Project.builder()
                .id(System.nanoTime())
                .userId(session.getUserId())
                .color(34)
                .isFavorite(false)
                .projectName("project name")
                .build();
        when(projectDao.getProjectById(project.getId())).thenReturn(Optional.of(project));
        EmptyResponse response = projectsService.deleteProject(project.getId(), uuid);
        verify(projectDao, times(1)).deleteProjectById(project.getId());
        assertNotNull(response);
    }

    @Test
    void testGetItemsByProjectId() throws PtpbException {
        Project project = Project.builder()
                .id(System.nanoTime())
                .userId(session.getUserId())
                .color(34)
                .isFavorite(false)
                .projectName("project name")
                .build();
        when(projectDao.getProjectById(project.getId())).thenReturn(Optional.of(project));
        List<ItemDto> responses = projectsService.getItemsByProjectId(project.getId(), uuid);
        verify(itemDao, times(1)).getItemsByProjectId(project.getId());
        assertNotNull(responses);
    }

    @Test
    void testCreateItem() throws PtpbException {
        long projectId = System.nanoTime();
        CreateItemRequest request = CreateItemRequest.builder()
                .content("content")
                .due(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .priority(2)
                .build();

        CreateItemResponse response = projectsService.createItem(request, projectId, uuid);
        ArgumentCaptor<Item> captor = ArgumentCaptor.forClass(Item.class);
        verify(itemDao, times(1)).insertItem(captor.capture());

        Item item = captor.getValue();
        assertNotNull(item);

        assertEquals(response.getId(), item.getId());
        assertEquals(request.getContent(), item.getContent());
        assertEquals(request.getDue(), item.getDue());
        assertEquals(request.getPriority(), item.getPriority());
        assertEquals(projectId, item.getProjectId());
        assertEquals(session.getUserId(), item.getUserId());
    }

    @Test
    void testDeleteItem() throws PtpbException {
        long itemId = System.nanoTime();
        long projectId = System.nanoTime();
        Item item = Item.builder()
                .id(itemId)
                .userId(session.getUserId())
                .projectId(projectId)
                .content("item text")
                .isCompleted(false)
                .due(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .priority(1)
                .build();
        when(itemDao.getItemById(itemId)).thenReturn(Optional.of(item));

        Response response = projectsService.deleteItemById(itemId, uuid);
        verify(itemDao, times(1)).deleteItemById(itemId);
        assertNotNull(response);
    }

    @Test
    void testGetItemById() throws PtpbException {
        long itemId = System.nanoTime();
        long projectId = System.nanoTime();
        Item item = Item.builder()
                .id(itemId)
                .userId(session.getUserId())
                .projectId(projectId)
                .content("item text")
                .isCompleted(false)
                .due(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .priority(1)
                .build();
        when(itemDao.getItemById(itemId)).thenReturn(Optional.of(item));

        GetItemResponse response = projectsService.getItemById(itemId, uuid);
        assertNotNull(response);
        assertEquals(itemId, response.getId());
        assertEquals(projectId, response.getProjectId());
        assertEquals(session.getUserId(), response.getUserId());
        assertEquals(item.getContent(), response.getContent());
        assertEquals(item.getPriority(), response.getPriority());
        assertEquals(item.getDue(), response.getDue());
    }
*/
}
