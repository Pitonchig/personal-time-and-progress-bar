package net.thumbtack.ptpb.handler.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import net.thumbtack.ptpb.db.session.Session;
import net.thumbtack.ptpb.db.session.SessionDao;
import net.thumbtack.ptpb.handler.common.EmptyResponse;
import net.thumbtack.ptpb.common.PtpbException;
import net.thumbtack.ptpb.handler.common.Response;
import net.thumbtack.ptpb.handler.project.dto.requests.CreateItemRequest;
import net.thumbtack.ptpb.handler.project.dto.requests.CreateProjectRequest;
import net.thumbtack.ptpb.handler.project.dto.responses.CreateItemResponse;
import net.thumbtack.ptpb.handler.project.dto.responses.CreateProjectResponse;
import net.thumbtack.ptpb.handler.project.dto.responses.GetItemResponse;
import net.thumbtack.ptpb.handler.project.dto.responses.GetProjectResponse;
import net.thumbtack.ptpb.rabbitmq.project.request.*;
import net.thumbtack.ptpb.rabbitmq.project.response.*;
import net.thumbtack.ptpb.rabbitmq.project.RabbitMqProjectService;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

import static net.thumbtack.ptpb.common.ErrorCode.SESSION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProjectsService {
    private final RabbitMqProjectService rabbitMqProjectService;
    private final SessionDao sessionDao;

    public List<? extends Response> getUserProjects(String uuid) throws PtpbException, JsonProcessingException {
        Session session = getSessionByUuid(uuid);
        GetProjectsAmqpRequest amqpRequest = new GetProjectsAmqpRequest(session.getUserId());
        List<GetProjectAmqpResponse> projects = rabbitMqProjectService.getProjectsByUserId(amqpRequest);
        return createGetProjectResponseList(projects);
    }

    public GetProjectResponse getProject(long projectId, String uuid) throws PtpbException, JsonProcessingException {
        Session session = getSessionByUuid(uuid);
        GetProjectAmqpRequest amqpRequest = new GetProjectAmqpRequest(projectId, session.getUserId());
        GetProjectAmqpResponse project = rabbitMqProjectService.getProjectById(amqpRequest);
        //TODO: PROJECT_NOT_FOUND
        return createGetProjectResponse(project);
    }

    public CreateProjectResponse createProject(CreateProjectRequest request, String uuid) throws PtpbException, JsonProcessingException {
        Session session = getSessionByUuid(uuid);
        CreateProjectAmqpRequest amqpRequest = CreateProjectAmqpRequest.builder()
                .userId(session.getUserId())
                .name(request.getName())
                .color(request.getColor())
                .isFavorite(request.isFavorite())
                .build();
        CreateProjectAmqpResponse project = rabbitMqProjectService.createProject(amqpRequest);
        return new CreateProjectResponse(project.getProjectId());
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

    public List<GetItemResponse> getItemsByProjectId(long projectId, String uuid) throws PtpbException, JsonProcessingException {
        Session session = getSessionByUuid(uuid);
        GetItemsAmqpRequest amqpRequest = GetItemsAmqpRequest.builder()
                .projectId(projectId)
                .userId(session.getUserId())
                .build();
        //TODO: PROJECT_NOT_FOUND
        List<GetItemAmqpResponse> amqpResponses = rabbitMqProjectService.getItems(amqpRequest);
        return createGetItemResponseList(amqpResponses);
    }

    public GetItemResponse getItemById(long itemId, String uuid) throws PtpbException, JsonProcessingException {
        Session session = getSessionByUuid(uuid);
        GetItemAmqpRequest amqpRequest = GetItemAmqpRequest.builder()
                .userId(session.getUserId())
                .itemId(itemId)
                .build();
        GetItemAmqpResponse amqpResponse = rabbitMqProjectService.getItem(amqpRequest);
        //TODO: ITEM_NOT_FOUND
        return createGetItemResponse(amqpResponse);
    }

    public CreateItemResponse createItem(CreateItemRequest request, long projectId, String uuid) throws PtpbException, JsonProcessingException {
        Session session = getSessionByUuid(uuid);

        CreateItemAmqpRequest amqpRequest = CreateItemAmqpRequest.builder()
                .userId(session.getUserId())
                .projectId(projectId)
                .content(request.getContent())
                .priority(request.getPriority())
                .due(request.getDue())
                .build();

        CreateItemAmqpResponse amqpResponse = rabbitMqProjectService.createItem(amqpRequest);
        return new CreateItemResponse(amqpResponse.getItemId());
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

    private GetItemResponse createGetItemResponse(GetItemAmqpResponse amqpResponse) {
        return GetItemResponse.builder()
                .id(amqpResponse.getId())
                .userId(amqpResponse.getUserId())
                .projectId(amqpResponse.getProjectId())
                .priority(amqpResponse.getPriority())
                .isCompleted(amqpResponse.isCompleted())
                .due(amqpResponse.getDue())
                .content(amqpResponse.getContent())
                .build();
    }

    private List<GetItemResponse> createGetItemResponseList(List<GetItemAmqpResponse> amqpResponses) {
        List<GetItemResponse> responses = new LinkedList<>();
        for (GetItemAmqpResponse amqpResponse : amqpResponses) {
            responses.add(createGetItemResponse(amqpResponse));
        }
        return responses;
    }

    private GetProjectResponse createGetProjectResponse(GetProjectAmqpResponse amqpResponse) {
        return GetProjectResponse.builder()
                .id(amqpResponse.getId())
                .name(amqpResponse.getName())
                .color(amqpResponse.getColor())
                .isFavorite(amqpResponse.isFavorite())
                .items(createGetItemResponseList(amqpResponse.getItems()))
                .build();
    }

    private List<GetProjectResponse> createGetProjectResponseList(List<GetProjectAmqpResponse> amqpResponses) {
        List<GetProjectResponse> responses = new LinkedList<>();
        for (GetProjectAmqpResponse amqpResponse : amqpResponses) {
            responses.add(createGetProjectResponse(amqpResponse));
        }
        return responses;
    }
}
