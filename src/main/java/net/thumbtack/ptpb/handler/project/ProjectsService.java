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
import net.thumbtack.ptpb.rabbitmq.project.response.*;
import net.thumbtack.ptpb.rabbitmq.project.RabbitMqProjectService;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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

        //GetProjectsAmqpRequest amqpRequest = new GetProjectsAmqpRequest(session.getUserId());
        //List<GetProjectAmqpResponse> projects = rabbitMqProjectService.getProjectsByUserId(amqpRequest);
        log.info("projects: {}", projects);
        return createGetProjectResponseList(projects);
    }

    public GetProjectResponse getProject(long projectId, String uuid) throws PtpbException, JsonProcessingException {
        Session session = getSessionByUuid(uuid);
        Optional<Project> result = projectDao.getProjectById(projectId);

        if(result.isEmpty()) {
            throw new PtpbException(PROJECT_NOT_FOUND);
        }
//        GetProjectAmqpRequest amqpRequest = new GetProjectAmqpRequest(projectId, session.getUserId());
//        GetProjectAmqpResponse project = rabbitMqProjectService.getProjectById(amqpRequest);
        return createGetProjectResponse(result.get());
    }

    public CreateProjectResponse createProject(CreateProjectRequest request, String uuid) throws PtpbException, JsonProcessingException {
        Session session = getSessionByUuid(uuid);
        Project project = Project.builder()
                .id(System.nanoTime())
                .userId(session.getUserId())
                .projectName(request.getName())
                .build();

        projectDao.insertProject(project);
//        CreateProjectAmqpRequest amqpRequest = CreateProjectAmqpRequest.builder()
//                .userId(session.getUserId())
//                .name(request.getName())
//                .color(request.getColor())
//                .isFavorite(request.isFavorite())
//                .build();
//        CreateProjectAmqpResponse project = rabbitMqProjectService.createProject(amqpRequest);
        return new CreateProjectResponse(project.getId());
    }

    public EmptyResponse deleteProject(long projectId, String uuid) throws PtpbException, JsonProcessingException {
        Session session = getSessionByUuid(uuid);
        DeleteProjectAmqpRequest amqpRequest = DeleteProjectAmqpRequest.builder()
                .userId(session.getUserId())
                .projectId(projectId)
                .build();
        rabbitMqProjectService.deleteProject(amqpRequest);
        //TODO: PROJECT_NOT_FOUND
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

    public CreateItemResponse createItem(CreateItemRequest request, long projectId, String uuid) throws PtpbException, JsonProcessingException {
        Session session = getSessionByUuid(uuid);
        Item item = Item.builder()
                .id(System.nanoTime())
                .userId(session.getUserId())
                .projectId(projectId)
                .content(request.getContent())
                .isCompleted(false)
                .build();

        itemDao.insertItem(item);
        log.info("item created id={}", item.getId());

//        CreateItemAmqpRequest amqpRequest = CreateItemAmqpRequest.builder()
//                .userId(session.getUserId())
//                .projectId(projectId)
//                .content(request.getContent())
//                .due(request.getDue())
//                .build();
//        CreateItemAmqpResponse amqpResponse = rabbitMqProjectService.createItem(amqpRequest);

        return new CreateItemResponse(item.getId());
    }

    public Response deleteItemById(long itemId, String uuid) throws PtpbException, JsonProcessingException {
        Session session = getSessionByUuid(uuid);
        DeleteItemAmqpRequest amqpRequest = DeleteItemAmqpRequest.builder()
                .itemId(itemId)
                .userId(session.getUserId())
                .build();
        //TODO: ITEM_NOT_FOUND
        rabbitMqProjectService.deleteItem(amqpRequest);
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


//    private GetItemResponse createGetItemResponse(GetItemAmqpResponse amqpResponse) {
//        return GetItemResponse.builder()
//                .id(amqpResponse.getId())
//                .userId(amqpResponse.getUserId())
//                .projectId(amqpResponse.getProjectId())
//                .priority(amqpResponse.getPriority())
//                .isCompleted(amqpResponse.isCompleted())
//                .due(amqpResponse.getDue())
//                .content(amqpResponse.getContent())
//                .build();
//    }

    private List<GetItemResponse> createGetItemResponseList(List<Item> items) {
        List<GetItemResponse> responses = new LinkedList<>();
        for (Item item : items) {
            responses.add(createGetItemResponse(item));
        }
        return responses;
    }


//    private List<GetItemResponse> createGetItemResponseList(List<GetItemAmqpResponse> amqpResponses) {
//        List<GetItemResponse> responses = new LinkedList<>();
//        for (GetItemAmqpResponse amqpResponse : amqpResponses) {
//            responses.add(createGetItemResponse(amqpResponse));
//        }
//        return responses;
//    }

//    private GetProjectResponse createGetProjectResponse(GetProjectAmqpResponse amqpResponse) {
//        return GetProjectResponse.builder()
//                .id(amqpResponse.getId())
//                .name(amqpResponse.getName())
//                .color(amqpResponse.getColor())
//                .isFavorite(amqpResponse.isFavorite())
//                .items(createGetItemResponseList(amqpResponse.getItems()))
//                .build();
//    }

//    private List<GetProjectResponse> createGetProjectResponseList(List<GetProjectAmqpResponse> amqpResponses) {
//        List<GetProjectResponse> responses = new LinkedList<>();
//        for (GetProjectAmqpResponse amqpResponse : amqpResponses) {
//            responses.add(createGetProjectResponse(amqpResponse));
//        }
//        return responses;
//    }


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

    public Response updateProject(long projectId, UpdateProjectRequest request, String uuid) throws PtpbException {
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

    public Response updateItem(long itemId, UpdateItemRequest request, String cookie) throws PtpbException {
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
