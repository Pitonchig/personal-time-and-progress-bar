package net.thumbtack.ptpb.rabbitmq.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.ptpb.common.PtpbException;
import net.thumbtack.ptpb.rabbitmq.common.ResponseWrapper;
import net.thumbtack.ptpb.rabbitmq.project.dto.request.*;
import net.thumbtack.ptpb.rabbitmq.project.dto.response.*;
import net.thumbtack.ptpb.rabbitmq.user.response.SyncUserTokenAmqpResponse;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static net.thumbtack.ptpb.common.ErrorCode.WRAPPER_ERROR;
import static net.thumbtack.ptpb.common.ErrorCode.WRAPPER_TIMEOUT;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMqProjectService {
    private final ObjectMapper objectMapper;
    private final RabbitTemplate template;
    private final String wrapperQueueName = "ptpb-wrapper";     //TODO: config param

    public SyncProjectsAmqpResponse syncProjects(SyncProjectsAmqpRequest request) throws JsonProcessingException, PtpbException {
        log.info("SyncProjectsAmqpRequest: request = {}", request);
        MessageProperties properties = MessagePropertiesBuilder.newInstance()
                .setContentType("application/json")
                .setHeader("type", request.getClass().getSimpleName())
                .build();

        String json = objectMapper.writeValueAsString(request);
        log.debug("send amqp request: {}", json);
        Message message = MessageBuilder.withBody(json.getBytes())
                .andProperties(properties)
                .build();

        Message response = template.sendAndReceive(wrapperQueueName, message);
        log.info("syncProjects: response = {}", response);
        if (response == null) {
            throw new PtpbException(WRAPPER_TIMEOUT);
        }

        ResponseWrapper responseWrapper = objectMapper.readValue(new String(response.getBody()), ResponseWrapper.class);
        if (!responseWrapper.isOk()) {
            throw new PtpbException(WRAPPER_ERROR);
        }
        return objectMapper.readValue(responseWrapper.getData(), SyncProjectsAmqpResponse.class);
    }
}
