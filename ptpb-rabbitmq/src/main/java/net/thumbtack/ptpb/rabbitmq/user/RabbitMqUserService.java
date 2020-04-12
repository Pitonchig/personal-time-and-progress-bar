package net.thumbtack.ptpb.rabbitmq.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMqUserService {
    private final ObjectMapper objectMapper;
    private final RabbitTemplate template;
    private final String wrapperQueueName = "ptpb-wrapper";     //TODO: config param

    public SyncUserAmqpResponse syncUser(SyncUserAmqpRequest request) throws JsonProcessingException {
        log.info("syncUSer: request = {}", request);
        MessageProperties properties = MessagePropertiesBuilder.newInstance()
                .setContentType("application/json")
                .setHeader("type", SyncUserAmqpRequest.class.getSimpleName())
                .build();

        Message message = MessageBuilder.withBody(objectMapper.writeValueAsString(request).getBytes())
                .andProperties(properties)
                .build();

        Message response = template.sendAndReceive(wrapperQueueName, message);
        log.info("syncUSer: response = {}", response);
        return objectMapper.readValue(new String(response.getBody()), SyncUserAmqpResponse.class);
    }

}
