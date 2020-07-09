package net.thumbtack.ptpb.handler.todoist;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.ptpb.common.PtpbException;
import net.thumbtack.ptpb.db.session.Session;
import net.thumbtack.ptpb.db.session.SessionDao;
import net.thumbtack.ptpb.db.user.Project;
import net.thumbtack.ptpb.db.user.User;
import net.thumbtack.ptpb.db.user.UserDao;
import net.thumbtack.ptpb.handler.common.EmptyResponse;
import net.thumbtack.ptpb.handler.todoist.dto.request.SyncProjectsRequest;
import net.thumbtack.ptpb.handler.todoist.dto.request.UpdateTodoistTokenRequest;
import net.thumbtack.ptpb.handler.todoist.dto.response.UpdateTodoistTokenResponse;
import net.thumbtack.ptpb.rabbitmq.project.RabbitMqProjectService;
import net.thumbtack.ptpb.rabbitmq.project.dto.request.SyncProjectsAmqpRequest;
import net.thumbtack.ptpb.rabbitmq.project.dto.response.SyncProjectsAmqpResponse;
import net.thumbtack.ptpb.rabbitmq.user.RabbitMqUserService;
import net.thumbtack.ptpb.rabbitmq.user.request.SyncUserTokenAmqpRequest;
import net.thumbtack.ptpb.rabbitmq.user.response.SyncUserTokenAmqpResponse;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

import java.util.List;

import static net.thumbtack.ptpb.common.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoistService {
    private final SessionDao sessionDao;
    private final UserDao userDao;
    private final RabbitMqUserService rabbitMqUserService;
    private final RabbitMqProjectService rabbitMqProjectService;

    public UpdateTodoistTokenResponse updateTodoistToken(UpdateTodoistTokenRequest request, String cookie) throws PtpbException, JsonProcessingException {
        User user = getUserBySessionUuid(cookie);

        SyncUserTokenAmqpRequest amqpRequest = SyncUserTokenAmqpRequest.builder()
                .userId(user.getId())
                .token(request.getToken())
                .build();
        log.info("amqpRequest={}", amqpRequest);

        SyncUserTokenAmqpResponse amqpResponse = rabbitMqUserService.syncUserToken(amqpRequest);
        log.info("amqpResponse={}", amqpResponse);

        user.setTodoistLinked(amqpResponse.isValid());
        userDao.insertUser(user);

        return UpdateTodoistTokenResponse.builder()
                .isTodoistLinked(user.isTodoistLinked())
                .build();
    }

    public EmptyResponse syncProjects(@Valid SyncProjectsRequest request, String cookie) throws PtpbException, JsonProcessingException {
        log.info("syncProjects: request={}, cookie={}", request, cookie);
        User user = getUserBySessionUuid(cookie);
        if (!user.isTodoistLinked()) {
            throw new PtpbException(TOKEN_NOT_FOUND);
        }

        List<Project> projects = user.getProjects();
        SyncProjectsAmqpRequest amqpRequest = SyncProjectsAmqpRequest.builder()
                .userId(user.getId())
                .toTodoist(request.isTo())
                .fromTodoist(request.isFrom())
                .projects(TodoistDtoConverter.toProjectAmqpDtoList(projects))
                .build();
        log.info("amqpRequest={}", amqpRequest);
        SyncProjectsAmqpResponse amqpResponse = rabbitMqProjectService.syncProjects(amqpRequest);
        if (amqpResponse == null) {
            throw new PtpbException(WRAPPER_TIMEOUT);
        }
        user.setProjects(TodoistDtoConverter.fromProjectAmqpDto(amqpResponse.getProjects()));
        userDao.insertUser(user);

        log.info("amqpResponse={}", amqpResponse);
        return new EmptyResponse();
    }


    private Session getSessionByUuid(String uuid) throws PtpbException {
        return sessionDao.getSessionByUuid(uuid)
                .orElseThrow(() -> new PtpbException(SESSION_NOT_FOUND));
    }

    private User getUserBySessionUuid(String uuid) throws PtpbException {
        Session session = getSessionByUuid(uuid);
        return userDao.getUserById(session.getUserId())
                .orElseThrow(() -> new PtpbException(USER_NOT_FOUND));
    }

}
