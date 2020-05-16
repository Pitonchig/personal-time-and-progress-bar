package net.thumbtack.ptpb.handler.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.ptpb.db.item.Item;
import net.thumbtack.ptpb.db.item.ItemDao;
import net.thumbtack.ptpb.db.project.Project;
import net.thumbtack.ptpb.db.project.ProjectDao;
import net.thumbtack.ptpb.db.session.Session;
import net.thumbtack.ptpb.db.session.SessionDao;
import net.thumbtack.ptpb.handler.common.EmptyResponse;
import net.thumbtack.ptpb.common.PtpbException;
import net.thumbtack.ptpb.handler.common.Response;
import net.thumbtack.ptpb.handler.project.dto.requests.CreateItemRequest;
import net.thumbtack.ptpb.handler.project.dto.requests.CreateProjectRequest;
import net.thumbtack.ptpb.handler.project.dto.requests.UpdateItemRequest;
import net.thumbtack.ptpb.handler.project.dto.requests.UpdateProjectRequest;
import net.thumbtack.ptpb.handler.project.dto.responses.*;
import net.thumbtack.ptpb.rabbitmq.project.request.*;
import net.thumbtack.ptpb.rabbitmq.project.RabbitMqProjectService;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static net.thumbtack.ptpb.common.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectsService {
    private final RabbitMqProjectService rabbitMqProjectService;
    private final SessionDao sessionDao;
    private final ProjectDao projectDao;
    private final ItemDao itemDao;

    public List<? extends Response> getUserProjects(String uuid) throws PtpbException, JsonProcessingException {
        Session session = getSessionByUuid(uuid);
        List<Project> projects = projectDao.getProjectsByUserId(session.getUserId());
        log.info("projects: {}", projects);
        return createGetProjectResponseList(projects);
    }

    public GetProjectResponse getProject(String projectId, String uuid) throws PtpbException, JsonProcessingException {
        getSessionByUuid(uuid);
        Optional<Project> result = projectDao.getProjectById(projectId);

        if(result.isEmpty()) {
            throw new PtpbException(PROJECT_NOT_FOUND);
        }
        return createGetProjectResponse(result.get());
    }

    public CreateProjectResponse createProject(CreateProjectRequest request, String uuid) throws PtpbException, JsonProcessingException {
        Session session = getSessionByUuid(uuid);
        Project project = Project.builder()
                .id(UUID.randomUUID().toString())
                .userId(session.getUserId())
                .projectName(request.getName())
                .build();
        projectDao.insertProject(project);
        return new CreateProjectResponse(project.getId());
    }

    public EmptyResponse deleteProject(String projectId, String uuid) throws PtpbException, JsonProcessingException {
        getSessionByUuid(uuid);
        if(projectDao.getProjectById(projectId).isEmpty()) {
            throw new PtpbException(PROJECT_NOT_FOUND);
        }
        projectDao.deleteProject(projectId);
        return new EmptyResponse();
    }

//    public List<GetItemResponse> getItemsByProjectId(long projectId, String uuid) throws PtpbException, JsonProcessingException {
//        Session session = getSessionByUuid(uuid);
//        GetItemsAmqpRequest amqpRequest = GetItemsAmqpRequest.builder()
//                .projectId(projectId)
//                .userId(session.getUserId())
//                .build();
//        //TODO: PROJECT_NOT_FOUND
//        List<GetItemAmqpResponse> amqpResponses = rabbitMqProjectService.getItems(amqpRequest);
//        return createGetItemResponseList(amqpResponses);
//    }
//
//    public GetItemResponse getItemById(long itemId, String uuid) throws PtpbException, JsonProcessingException {
//        Session session = getSessionByUuid(uuid);
//        GetItemAmqpRequest amqpRequest = GetItemAmqpRequest.builder()
//                .userId(session.getUserId())
//                .itemId(itemId)
//                .build();
//        GetItemAmqpResponse amqpResponse = rabbitMqProjectService.getItem(amqpRequest);
//        //TODO: ITEM_NOT_FOUND
//        return createGetItemResponse(amqpResponse);
//    }

    public CreateItemResponse createItem(CreateItemRequest request, String projectId, String uuid) throws PtpbException, JsonProcessingException {
        Session session = getSessionByUuid(uuid);
        Item item = Item.builder()
                .id(UUID.randomUUID().toString())
                .userId(session.getUserId())
                .projectId(projectId)
                .content(request.getContent())
                .isCompleted(false)
                .build();

        itemDao.insertItem(item);
        log.info("item created id={}", item.getId());
        return new CreateItemResponse(item.getId());
    }

    public Response deleteItemById(String itemId, String uuid) throws PtpbException, JsonProcessingException {
        getSessionByUuid(uuid);
        if(itemDao.getItemById(itemId).isEmpty() ) {
            throw new PtpbException(ITEM_NOT_FOUND);
        }
        itemDao.deleteItemById(itemId);
        return new EmptyResponse();
    }

    private Session getSessionByUuid(String uuid) throws PtpbException {
        return sessionDao.getSessionByUuid(uuid).orElseThrow(() -> new PtpbException(SESSION_NOT_FOUND));
    }

    private GetItemResponse createGetItemResponse(Item item) {
    return GetItemResponse.builder()
            .id(item.getId())
            .userId(item.getUserId())
            .projectId(item.getProjectId())
            .isCompleted(item.isCompleted())
            .content(item.getContent())
            .build();
    }

    private List<GetItemResponse> createGetItemResponseList(List<Item> items) {
        List<GetItemResponse> responses = new LinkedList<>();
        for (Item item : items) {
            responses.add(createGetItemResponse(item));
        }
        return responses;
    }

    private GetProjectResponse createGetProjectResponse(Project project) {
        List<Item> items = itemDao.getItemsByProjectId(project.getId());
        return GetProjectResponse.builder()
                .id(project.getId())
                .name(project.getProjectName())
                .items(createGetItemResponseList(items))
                .build();
    }

    private List<GetProjectResponse> createGetProjectResponseList(List<Project> projects) {
        List<GetProjectResponse> responses = new LinkedList<>();
        for (Project project : projects) {
            responses.add(createGetProjectResponse(project));
        }
        return responses;
    }

    public Response updateProject(String projectId, UpdateProjectRequest request, String uuid) throws PtpbException {
        Session session = getSessionByUuid(uuid);
        Optional<Project> result = projectDao.getProjectById(projectId);
        if(result.isEmpty()) {
            throw new PtpbException(PROJECT_NOT_FOUND);
        }

        Project project = result.get();
        project.setProjectName(request.getName());
        projectDao.updateProject(project);

        return new EmptyResponse();
    }

    public Response updateItem(String itemId, UpdateItemRequest request, String cookie) throws PtpbException {
        Session session = getSessionByUuid(cookie);
        Optional<Item> result = itemDao.getItemById(itemId);
        if(result.isEmpty())  {
            throw new PtpbException(ITEM_NOT_FOUND);
        }

        Item item = result.get();
        item.setContent(request.getContent());
        item.setCompleted(request.isCompleted());
        itemDao.updateItem(item);
        return new EmptyResponse();
    }
}
