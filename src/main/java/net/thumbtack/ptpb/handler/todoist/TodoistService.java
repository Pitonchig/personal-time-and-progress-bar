package net.thumbtack.ptpb.handler.todoist;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.ptpb.common.PtpbException;
import net.thumbtack.ptpb.db.session.Session;
import net.thumbtack.ptpb.db.session.SessionDao;
import net.thumbtack.ptpb.db.todoist.Todoist;
import net.thumbtack.ptpb.db.todoist.TodoistDao;
import net.thumbtack.ptpb.handler.common.EmptyResponse;
import net.thumbtack.ptpb.handler.todoist.dto.request.SyncProjectsRequest;
import net.thumbtack.ptpb.handler.todoist.dto.request.UpdateTodoistTokenRequest;
import net.thumbtack.ptpb.handler.todoist.dto.response.UpdateTodoistTokenResponse;
import net.thumbtack.ptpb.rabbitmq.project.RabbitMqProjectService;
import net.thumbtack.ptpb.rabbitmq.project.request.SyncProjectsAmqpRequest;
import net.thumbtack.ptpb.rabbitmq.project.response.SyncProjectsAmqpResponse;
import net.thumbtack.ptpb.rabbitmq.user.RabbitMqUserService;
import net.thumbtack.ptpb.rabbitmq.user.request.SyncUserTokenAmqpRequest;
import net.thumbtack.ptpb.rabbitmq.user.response.SyncUserTokenAmqpResponse;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

import java.util.Optional;

import static net.thumbtack.ptpb.common.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoistService {
    private final TodoistDao todoistDao;
    private final SessionDao sessionDao;
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

        return UpdateTodoistTokenResponse.builder()
                .isTodoistLinked(amqpResponse.isValid())
                .build();
    }

    public EmptyResponse syncProjects(@Valid SyncProjectsRequest request, String cookie) throws PtpbException, JsonProcessingException {
        log.info("syncProjects: request={}, cookie={}", request, cookie);
        Session session = getSessionByUuid(cookie);
        Optional<Todoist> todoist = todoistDao.getTodoistByUserUuid(session.getUserId());

        if (todoist.isEmpty()) {
            throw new PtpbException(TOKEN_NOT_FOUND);
        }

        SyncProjectsAmqpRequest amqpRequest = SyncProjectsAmqpRequest.builder()
                .userId(session.getUserId())
                .build();
        log.info("amqpRequest={}", amqpRequest);
        SyncProjectsAmqpResponse amqpResponse = rabbitMqProjectService.syncProjects(amqpRequest);
        log.info("amqpResponse={}", amqpResponse);
        return new EmptyResponse();
    }


    private Session getSessionByUuid(String uuid) throws PtpbException {
        return sessionDao.getSessionByUuid(uuid).orElseThrow(() -> new PtpbException(SESSION_NOT_FOUND));
    }
}
