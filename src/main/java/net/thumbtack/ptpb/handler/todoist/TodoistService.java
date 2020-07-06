package net.thumbtack.ptpb.handler.todoist;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.ptpb.common.PtpbException;
import net.thumbtack.ptpb.db.item.Item;
import net.thumbtack.ptpb.db.item.ItemDao;
import net.thumbtack.ptpb.db.project.Project;
import net.thumbtack.ptpb.db.project.ProjectDao;
import net.thumbtack.ptpb.db.session.Session;
import net.thumbtack.ptpb.db.session.SessionDao;
import net.thumbtack.ptpb.db.services.Services;
import net.thumbtack.ptpb.db.services.ServicesDao;
import net.thumbtack.ptpb.handler.common.EmptyResponse;
import net.thumbtack.ptpb.handler.todoist.dto.request.SyncProjectsRequest;
import net.thumbtack.ptpb.handler.todoist.dto.request.UpdateTodoistTokenRequest;
import net.thumbtack.ptpb.handler.todoist.dto.response.UpdateTodoistTokenResponse;
import net.thumbtack.ptpb.rabbitmq.project.RabbitMqProjectService;
import net.thumbtack.ptpb.rabbitmq.project.dto.ItemDto;
import net.thumbtack.ptpb.rabbitmq.project.dto.ProjectDto;
import net.thumbtack.ptpb.rabbitmq.project.request.SyncProjectsAmqpRequest;
import net.thumbtack.ptpb.rabbitmq.project.response.SyncProjectsAmqpResponse;
import net.thumbtack.ptpb.rabbitmq.user.RabbitMqUserService;
import net.thumbtack.ptpb.rabbitmq.user.request.SyncUserTokenAmqpRequest;
import net.thumbtack.ptpb.rabbitmq.user.response.SyncUserTokenAmqpResponse;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static net.thumbtack.ptpb.common.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoistService {
    private final ServicesDao servicesDao;
    private final SessionDao sessionDao;
    private final ProjectDao projectDao;
    private final ItemDao itemDao;
    private final RabbitMqUserService rabbitMqUserService;
    private final RabbitMqProjectService rabbitMqProjectService;

    public UpdateTodoistTokenResponse updateTodoistToken(UpdateTodoistTokenRequest request, String cookie) throws PtpbException, JsonProcessingException {
        Session session = getSessionByUuid(cookie);

        SyncUserTokenAmqpRequest amqpRequest = SyncUserTokenAmqpRequest.builder()
                .userId(session.getUserId())
                .token(request.getToken())
                .build();
        log.info("amqpRequest={}", amqpRequest);

        SyncUserTokenAmqpResponse amqpResponse = rabbitMqUserService.syncUserToken(amqpRequest);
        log.info("amqpResponse={}", amqpResponse);

        Services services = Services.builder()
                .userId(session.getUserId())
                .isTodoistLinked(amqpResponse.isValid())
                .build();
        servicesDao.insertServices(services);

        return UpdateTodoistTokenResponse.builder()
                .isTodoistLinked(services.isTodoistLinked())
                .build();
    }

    public EmptyResponse syncProjects(@Valid SyncProjectsRequest request, String cookie) throws PtpbException, JsonProcessingException {
        log.info("syncProjects: request={}, cookie={}", request, cookie);
        Session session = getSessionByUuid(cookie);
        Optional<Services> todoist = servicesDao.getServicesByUserUuid(session.getUserId());

        if (todoist.isEmpty()) {
            throw new PtpbException(TOKEN_NOT_FOUND);
        }

        List<Project> projects = projectDao.getProjectsByUserId(session.getUserId());


        SyncProjectsAmqpRequest amqpRequest = SyncProjectsAmqpRequest.builder()
                .userId(session.getUserId())
                .toTodoist(request.isTo())
                .fromTodoist(request.isFrom())
                .projects(projectsToDtoList(projects))
                .build();
        log.info("amqpRequest={}", amqpRequest);
        SyncProjectsAmqpResponse amqpResponse = rabbitMqProjectService.syncProjects(amqpRequest);
        log.info("amqpResponse={}", amqpResponse);
        return new EmptyResponse();
    }


    private Session getSessionByUuid(String uuid) throws PtpbException {
        return sessionDao.getSessionByUuid(uuid).orElseThrow(() -> new PtpbException(SESSION_NOT_FOUND));
    }

    private List<ProjectDto> projectsToDtoList(List<Project> projects) {
        List<ProjectDto> dtoList = new LinkedList<>();
        for (Project project : projects) {
            dtoList.add(projectToDto(project));
        }
        return dtoList;
    }

    private ProjectDto projectToDto(Project project) {
        List<ItemDto> itemDtoList = itemsToItemDtoList(itemDao.getItemsByProjectId(project.getId()));
        ProjectDto projectDto = ProjectDto.builder()
                .id(project.getId())
                .name(project.getProjectName())
                .items(itemDtoList)
                .build();
        return projectDto;
    }

    private List<ItemDto> itemsToItemDtoList(List<Item> items) {
        List<ItemDto> itemDtoList = new LinkedList<>();
        for (Item item : items) {
            itemDtoList.add(itemToItemDto(item));
        }
        return itemDtoList;
    }

    private ItemDto itemToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .content(item.getContent())
                .isCompleted(item.isCompleted())
                .start(item.getStart())
                .finish(item.getFinish())
                .projectId(item.getUserId())
                .projectId(item.getProjectId())
                .build();
    }
}
