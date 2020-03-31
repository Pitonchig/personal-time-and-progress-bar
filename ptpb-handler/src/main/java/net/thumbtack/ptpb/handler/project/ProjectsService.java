package net.thumbtack.ptpb.handler.project;

import lombok.RequiredArgsConstructor;
import net.thumbtack.ptpb.db.item.Item;
import net.thumbtack.ptpb.db.item.ItemDao;
import net.thumbtack.ptpb.db.project.Project;
import net.thumbtack.ptpb.db.project.ProjectDao;
import net.thumbtack.ptpb.db.session.Session;
import net.thumbtack.ptpb.db.session.SessionDao;
import net.thumbtack.ptpb.db.user.UserDao;
import net.thumbtack.ptpb.handler.common.EmptyResponse;
import net.thumbtack.ptpb.handler.common.PtpbException;
import net.thumbtack.ptpb.handler.common.Response;
import net.thumbtack.ptpb.handler.project.dto.ItemDto;
import net.thumbtack.ptpb.handler.project.dto.requests.CreateProjectRequest;
import net.thumbtack.ptpb.handler.project.dto.requests.CreateItemRequest;
import net.thumbtack.ptpb.handler.project.dto.responses.CreateProjectResponse;
import net.thumbtack.ptpb.handler.project.dto.responses.CreateItemResponse;
import net.thumbtack.ptpb.handler.project.dto.responses.GetProjectResponse;
import net.thumbtack.ptpb.handler.project.dto.responses.GetItemResponse;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static net.thumbtack.ptpb.handler.common.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ProjectsService {
    private final SessionDao sessionDao;
    private final UserDao userDao;
    private final ProjectDao projectDao;
    private final ItemDao itemDao;


    public List<? extends Response> getUserProjects(String uuid) throws PtpbException {
        Session session = getSessionByUuid(uuid);
        List<Project> projects = projectDao.getProjectsByUserId(session.getUserId());
        List<GetProjectResponse> responses = new LinkedList<>();

        for (Project project : projects) {
            List<Item> children = itemDao.getItemsByProjectId(project.getId());
            GetProjectResponse response = GetProjectResponse.builder()
                    .id(project.getId())
                    .name(project.getProjectName())
                    .color(project.getColor())
                    .isFavorite(project.isFavorite())
                    .items(itemsToItemsDto(children))
                    .build();
            responses.add(response);
        }
        return responses;
    }

    public CreateProjectResponse createProject(CreateProjectRequest request, String uuid) throws PtpbException {
        Session session = getSessionByUuid(uuid);

        Project project = Project.builder()
                .id(System.nanoTime())
                .projectName(request.getName())
                .userId(session.getUserId())
                .color(request.getColor())
                .isFavorite(request.isFavorite())
                .build();
        projectDao.insertProject(project);

        return new CreateProjectResponse(project.getId());
    }

    public EmptyResponse deleteProject(long projectId, String uuid) throws PtpbException {
        Session session = getSessionByUuid(uuid);
        checkProjectOwnerAndReturn(projectId, session);
        projectDao.deleteProjectById(projectId);
        return new EmptyResponse();
    }

    public GetProjectResponse getProject(long projectId, String uuid) throws PtpbException {
        Session session = getSessionByUuid(uuid);
        Project project = checkProjectOwnerAndReturn(projectId, session);

        List<Item> children = itemDao.getItemsByProjectId(project.getId());
        GetProjectResponse response = GetProjectResponse.builder()
                .id(project.getId())
                .name(project.getProjectName())
                .color(project.getColor())
                .isFavorite(project.isFavorite())
                .items(itemsToItemsDto(children))
                .build();

        return response;
    }

    public List<ItemDto> getItemsByProjectId(long projectId, String uuid) throws PtpbException {
        Session session = getSessionByUuid(uuid);
        checkProjectOwnerAndReturn(projectId, session);
        List<Item> children = itemDao.getItemsByProjectId(projectId);
        return itemsToItemsDto(children);
    }

    public CreateItemResponse createItem(CreateItemRequest request, long projectId, String uuid) throws PtpbException {
        Session session = getSessionByUuid(uuid);
        Item item = Item.builder()
                .id(System.nanoTime())
                .userId(session.getUserId())
                .projectId(projectId)
                .priority(request.getPriority())
                .isCompleted(false)
                .due(request.getDue())
                .content(request.getContent())
                .build();
        itemDao.insertItem(item);
        return new CreateItemResponse(item.getId());
    }

    public Response deleteItemById(long itemId, String uuid) throws PtpbException {
        Session session = getSessionByUuid(uuid);
        checkItemOwnerAndReturn(itemId, session);
        itemDao.deleteItemById(itemId);
        return new EmptyResponse();
    }

    public GetItemResponse getItemById(long itemId, String uuid) throws PtpbException {
        Session session = getSessionByUuid(uuid);
        Item item = checkItemOwnerAndReturn(itemId, session);
        return GetItemResponse.builder()
                .id(item.getId())
                .userId(session.getUserId())
                .projectId(item.getProjectId())
                .priority(item.getPriority())
                .isCompleted(item.isCompleted())
                .due(item.getDue())
                .content(item.getContent())
                .build();
    }

//    private void checkSessionIsValid(String uuid) throws PtpbException {
//        if (!sessionDao.isOnline(uuid)) {
//            throw new PtpbException(SESSION_NOT_FOUND);
//        }
//    }

    private Session getSessionByUuid(String uuid) throws PtpbException {
        return sessionDao.getSessionByUuid(uuid).orElseThrow(() -> new PtpbException(SESSION_NOT_FOUND));
    }

//    private User getUserBySessionUuid(String uuid) throws PtpbException {
//        Session session = getSessionByUuid(uuid);
//        return userDao.getUserById(session.getUserId()).orElseThrow(() -> new PtpbException(USER_NOT_FOUND));
//    }

    private List<ItemDto> itemsToItemsDto(List<Item> items) {
        List<ItemDto> list = new LinkedList<>();
        for (Item item : items) {
            ItemDto dto = ItemDto.builder()
                    .id(item.getId())
                    .userId(item.getUserId())
                    .projectId(item.getProjectId())
                    .content(item.getContent())
                    .due(item.getDue())
                    .priority(item.getPriority())
                    .build();
            list.add(dto);
        }
        return list;
    }

    private Project checkProjectOwnerAndReturn(long projectId, Session session) throws PtpbException {
        Optional<Project> projectResult = projectDao.getProjectById(projectId);
        Project project = projectResult.orElseThrow(() -> new PtpbException(PROJECT_NOT_FOUND));
        if (project.getUserId() != session.getUserId()) {
            throw new PtpbException(USER_NOT_OWNER);
        }
        return project;
    }

    private Item checkItemOwnerAndReturn(long itemId, Session session) throws PtpbException {
        Optional<Item> itemResult = itemDao.getItemById(itemId);
        Item item = itemResult.orElseThrow(() -> new PtpbException(ITEM_NOT_FOUND));
        if (item.getUserId() != session.getUserId()) {
            throw new PtpbException(USER_NOT_OWNER);
        }
        return item;
    }

}
