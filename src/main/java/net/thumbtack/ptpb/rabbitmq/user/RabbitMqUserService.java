package net.thumbtack.ptpb.rabbitmq.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.ptpb.common.PtpbException;
import net.thumbtack.ptpb.rabbitmq.common.ResponseWrapper;
import net.thumbtack.ptpb.rabbitmq.user.request.SyncUserTokenAmqpRequest;
import net.thumbtack.ptpb.rabbitmq.user.response.SyncUserTokenAmqpResponse;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static net.thumbtack.ptpb.common.ErrorCode.WRAPPER_ERROR;
import static net.thumbtack.ptpb.common.ErrorCode.WRAPPER_TIMEOUT;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMqUserService {
    private final ObjectMapper objectMapper;
    private final RabbitTemplate template;
    private final String wrapperQueueName = "ptpb-wrapper";     //TODO: config param


    public SyncUserTokenAmqpResponse syncUserToken(SyncUserTokenAmqpRequest request) throws JsonProcessingException, PtpbException {
        log.info("syncUserToken: request = {}", request);
        MessageProperties properties = MessagePropertiesBuilder.newInstance()
                .setContentType("application/json")
                .setHeader("type", SyncUserTokenAmqpRequest.class.getSimpleName())
                .build();

        Message message = MessageBuilder.withBody(objectMapper.writeValueAsString(request).getBytes())
                .andProperties(properties)
                .build();

        Message response = template.sendAndReceive(wrapperQueueName, message);
        if (response == null) {
            throw new PtpbException(WRAPPER_TIMEOUT);
        }
        log.info("syncUserToken: response = {}", response);

        ResponseWrapper responseWrapper = objectMapper.readValue(new String(response.getBody()), ResponseWrapper.class);
        if(!responseWrapper.isOk()) {
            throw new PtpbException(WRAPPER_ERROR);
        }
        return objectMapper.readValue(responseWrapper.getData(), SyncUserTokenAmqpResponse.class);

    }
}
