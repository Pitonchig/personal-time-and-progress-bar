package net.thumbtack.ptpb.handler.project;

import net.thumbtack.ptpb.db.session.Session;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ProjectsServiceTest {
    private ProjectsService projectsService;
    private String uuid;
    private Session session;

    /*
    @MockBean
    private SessionDao sessionDao;
    @MockBean
    private UserDao userDao;
    @MockBean
    private ProjectDao projectDao;
    @MockBean
    private ItemDao itemDao;


    @BeforeEach
    void setup() {
        uuid = UUID.randomUUID().toString();
        session = Session.builder()
                .uuid(uuid)
                .userId(System.nanoTime())
                .isExpired(false)
                .dateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
        projectsService = new ProjectsService(sessionDao, userDao, projectDao, itemDao);
        when(sessionDao.getSessionByUuid(uuid)).thenReturn(Optional.of(session));
    }


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
