package net.thumbtack.ptpb.rabbitmq.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.ptpb.common.PtpbException;
import net.thumbtack.ptpb.rabbitmq.user.request.SyncUserAmqpRequest;
import net.thumbtack.ptpb.rabbitmq.user.response.SyncUserAmqpResponse;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static net.thumbtack.ptpb.common.ErrorCode.WRAPPER_TIMEOUT;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMqUserService {
    private final ObjectMapper objectMapper;
    private final RabbitTemplate template;
    private final String wrapperQueueName = "ptpb-wrapper";     //TODO: config param

    public SyncUserAmqpResponse syncUser(SyncUserAmqpRequest request) throws JsonProcessingException, PtpbException {
        log.info("syncUSer: request = {}", request);
        MessageProperties properties = MessagePropertiesBuilder.newInstance()
                .setContentType("application/json")
                .setHeader("type", SyncUserAmqpRequest.class.getSimpleName())
                .build();

        Message message = MessageBuilder.withBody(objectMapper.writeValueAsString(request).getBytes())
                .andProperties(properties)
                .build();

        Message response = template.sendAndReceive(wrapperQueueName, message);
        if (response == null) {
            throw new PtpbException(WRAPPER_TIMEOUT);
        }
        log.info("syncUSer: response = {}", response);
        String json = objectMapper.readValue(new String(response.getBody()), String.class);
        return objectMapper.readValue(json, SyncUserAmqpResponse.class);
        //return objectMapper.readValue(new String(response.getBody()), SyncUserAmqpResponse.class);
    }

}
