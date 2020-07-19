package net.thumbtack.ptpb.handler.todoist;

import net.thumbtack.ptpb.db.session.Session;
import net.thumbtack.ptpb.db.session.SessionDao;
import net.thumbtack.ptpb.db.user.Item;
import net.thumbtack.ptpb.db.user.Project;
import net.thumbtack.ptpb.db.user.User;
import net.thumbtack.ptpb.db.user.UserDao;
import net.thumbtack.ptpb.handler.common.EmptyResponse;
import net.thumbtack.ptpb.handler.project.ProjectsService;
import net.thumbtack.ptpb.handler.project.dto.ItemDto;
import net.thumbtack.ptpb.handler.project.dto.response.ProjectResponse;
import net.thumbtack.ptpb.handler.todoist.dto.request.SyncProjectsRequest;
import net.thumbtack.ptpb.handler.todoist.dto.request.UpdateTodoistTokenRequest;
import net.thumbtack.ptpb.handler.todoist.dto.response.UpdateTodoistTokenResponse;
import net.thumbtack.ptpb.handler.user.dto.responses.RegisterUserResponse;
import net.thumbtack.ptpb.rabbitmq.project.RabbitMqProjectService;
import net.thumbtack.ptpb.rabbitmq.project.dto.ItemAmqpDto;
import net.thumbtack.ptpb.rabbitmq.project.dto.ProjectAmqpDto;
import net.thumbtack.ptpb.rabbitmq.project.dto.request.SyncProjectsAmqpRequest;
import net.thumbtack.ptpb.rabbitmq.project.dto.response.SyncProjectsAmqpResponse;
import net.thumbtack.ptpb.rabbitmq.user.RabbitMqUserService;
import net.thumbtack.ptpb.rabbitmq.user.request.SyncUserTokenAmqpRequest;
import net.thumbtack.ptpb.rabbitmq.user.response.SyncUserTokenAmqpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class TodoistServiceTest {

    private String sessionUuid;
    private Session session;
    private TodoistService todoistService;

    @MockBean
    private SessionDao sessionDao;
    @MockBean
    private UserDao userDao;
    @MockBean
    private RabbitMqUserService rabbitMqUserService;
    @MockBean
    private RabbitMqProjectService rabbitMqProjectService;

    @BeforeEach
    void setup() {
        sessionUuid = UUID.randomUUID().toString();
        session = Session.builder()
                .uuid(sessionUuid)
                .userId(UUID.randomUUID().toString())
                .isExpired(false)
                .dateTime(ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
        todoistService = new TodoistService(sessionDao, userDao, rabbitMqUserService, rabbitMqProjectService);
        when(sessionDao.getSessionByUuid(sessionUuid)).thenReturn(Optional.of(session));
    }


    @Test
    void testUpdateTodoistToken() throws Exception {
        String token = UUID.randomUUID().toString();
        String userUuid = session.getUserId();
        User user = User.builder()
                .id(userUuid)
                .name("User")
                .isTodoistLinked(false)
                .build();
        when(userDao.getUserById(userUuid)).thenReturn(Optional.ofNullable(user));

        SyncUserTokenAmqpResponse amqpResponse = SyncUserTokenAmqpResponse.builder()
                .isValid(true)
                .build();
        when(rabbitMqUserService.syncUserToken(any(SyncUserTokenAmqpRequest.class))).thenReturn(amqpResponse);

        UpdateTodoistTokenRequest request = UpdateTodoistTokenRequest.builder()
                .token(token)
                .build();

        UpdateTodoistTokenResponse response = todoistService.updateTodoistToken(request, sessionUuid);

        ArgumentCaptor<SyncUserTokenAmqpRequest> amqpRequestArgumentCaptor = ArgumentCaptor.forClass(SyncUserTokenAmqpRequest.class);
        verify(rabbitMqUserService, times(1)).syncUserToken(amqpRequestArgumentCaptor.capture());

        assertNotNull(response);
        assertTrue(response.isTodoistLinked());

        SyncUserTokenAmqpRequest amqpRequest = amqpRequestArgumentCaptor.getValue();
        assertNotNull(amqpRequest);
        assertEquals(userUuid, amqpRequest.getUserId());
        assertEquals(request.getToken(), amqpRequest.getToken());
    }


    @Test
    void testSyncProjects() throws Exception {
        boolean isDoImport = true;
        boolean isDoExport = true;

        String userUuid = session.getUserId();

        Project project1 = Project.builder()
                .id(UUID.randomUUID().toString())
                .projectName("project1")
                .item(Item.builder().id(UUID.randomUUID().toString()).content("item1-1").build())
                .item(Item.builder().id(UUID.randomUUID().toString()).content("item1-2").build())
                .build();

        Project project2 = Project.builder()
                .id(UUID.randomUUID().toString())
                .projectName("project2")
                .item(Item.builder().id(UUID.randomUUID().toString()).content("item2-1").build())
                .item(Item.builder().id(UUID.randomUUID().toString()).content("item2-2").build())
                .item(Item.builder().id(UUID.randomUUID().toString()).content("item2-3").build())
                .build();
        List<Project> userProjectList = Arrays.asList(project1, project2);

        User user = User.builder()
                .id(userUuid)
                .name("User")
                .isTodoistLinked(true)
                .projects(userProjectList)
                .build();

        when(userDao.getUserById(userUuid)).thenReturn(Optional.ofNullable(user));

        ItemAmqpDto amqpItem = ItemAmqpDto.builder()
                .id(UUID.randomUUID().toString())
                .content("amqpItem1-1")
                .isCompleted(false)
                .build();
        ProjectAmqpDto amqpProject = ProjectAmqpDto.builder()
                .id(UUID.randomUUID().toString())
                .name("amqpProject")
                .item(amqpItem)
                .build();
        SyncProjectsAmqpResponse amqpResponse = SyncProjectsAmqpResponse.builder()
                .fromTodoist(isDoImport)
                .toTodoist(isDoExport)
                .userId(user.getId())
                .project(amqpProject)
                .build();
        when(rabbitMqProjectService.syncProjects(any(SyncProjectsAmqpRequest.class))).thenReturn(amqpResponse);

        SyncProjectsRequest request = SyncProjectsRequest.builder()
                .to(isDoExport)
                .from(isDoImport)
                .build();

        EmptyResponse response = todoistService.syncProjects(request, sessionUuid);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDao, times(1)).insertUser(userArgumentCaptor.capture());
        assertNotNull(response);

        ArgumentCaptor<SyncProjectsAmqpRequest> amqpRequestArgumentCaptor = ArgumentCaptor.forClass(SyncProjectsAmqpRequest.class);
        verify(rabbitMqProjectService, times(1)).syncProjects(amqpRequestArgumentCaptor.capture());

        SyncProjectsAmqpRequest amqpRequest = amqpRequestArgumentCaptor.getValue();
        assertNotNull(amqpRequest);
        assertEquals(user.getId(), amqpRequest.getUserId());
        assertEquals(isDoExport, amqpRequest.isToTodoist());
        assertEquals(isDoImport, amqpRequest.isFromTodoist());
        assertEquals(userProjectList.size(), amqpRequest.getProjects().size());
        List<ProjectAmqpDto> amqpDtoList = amqpRequest.getProjects();

        for (ProjectAmqpDto projectAmqpDto : amqpDtoList) {
            Optional<Project> optional = userProjectList.stream().filter(p -> projectAmqpDto.getId().equals(p.getId())).findFirst();
            assertTrue(optional.isPresent());
            Project project = optional.get();

            assertEquals(project.getProjectName(), projectAmqpDto.getName());
            List<String> responseItems = projectAmqpDto.getItems().stream().map(ItemAmqpDto::getId).collect(Collectors.toList());
            List<String> projectItems = project.getItems().stream().map(Item::getId).collect(Collectors.toList());
            assertEquals(responseItems.size(), projectItems.size());
            assertTrue(responseItems.containsAll(projectItems));
        }

        User resultUser = userArgumentCaptor.getValue();
        assertNotNull(resultUser);
        assertEquals(userUuid, resultUser.getId());
    }
}
