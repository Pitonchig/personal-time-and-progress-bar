package net.thumbtack.ptpb.rabbitmq.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.ptpb.rabbitmq.project.request.*;
import net.thumbtack.ptpb.rabbitmq.project.response.*;
import net.thumbtack.ptpb.rabbitmq.user.SyncUserAmqpRequest;
import net.thumbtack.ptpb.rabbitmq.user.SyncUserAmqpResponse;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMqProjectService {
    private final ObjectMapper objectMapper;
    private final RabbitTemplate template;
    private final String wrapperQueueName = "ptpb-wrapper";     //TODO: config param

    public SyncUserAmqpResponse getProject(SyncUserAmqpRequest request) throws JsonProcessingException {
        log.info("syncUSer: request = {}", request);
        MessageProperties properties = MessagePropertiesBuilder.newInstance()
                .setContentType("application/json")
                .setHeader("type", request.getClass().getSimpleName())
                .build();

        Message message = MessageBuilder.withBody(objectMapper.writeValueAsString(request).getBytes())
                .andProperties(properties)
                .build();

        Message response = template.sendAndReceive(wrapperQueueName, message);
        log.info("syncUSer: response = {}", response);
        return objectMapper.readValue(new String(response.getBody()), SyncUserAmqpResponse.class);
    }

    public List<GetProjectAmqpResponse> getProjectsByUserId(GetProjectsAmqpRequest request) throws JsonProcessingException {
        log.info("getProjectsByUserId: request = {}", request);
        MessageProperties properties = MessagePropertiesBuilder.newInstance()
                .setContentType("application/json")
                .setHeader("type", request.getClass().getSimpleName())
                .build();

        Message message = MessageBuilder.withBody(objectMapper.writeValueAsString(request).getBytes())
                .andProperties(properties)
                .build();

        Message response = template.sendAndReceive(wrapperQueueName, message);
        log.info("getProjectsByUserId: response = {}", response);
        return Arrays.asList(objectMapper.readValue(new String(response.getBody()), GetProjectAmqpResponse[].class));
    }

    public GetProjectAmqpResponse getProjectById(GetProjectAmqpRequest request) throws JsonProcessingException {
        log.info("getProjectById: request = {}", request);
        MessageProperties properties = MessagePropertiesBuilder.newInstance()
                .setContentType("application/json")
                .setHeader("type", request.getClass().getSimpleName())
                .build();

        Message message = MessageBuilder.withBody(objectMapper.writeValueAsString(request).getBytes())
                .andProperties(properties)
                .build();

        Message response = template.sendAndReceive(wrapperQueueName, message);
        log.info("getProjectById: response = {}", response);
        return objectMapper.readValue(new String(response.getBody()), GetProjectAmqpResponse.class);
    }

    public CreateProjectAmqpResponse createProject(CreateProjectAmqpRequest amqpRequest) throws JsonProcessingException {
        log.info("createProject: request = {}", amqpRequest);
        MessageProperties properties = MessagePropertiesBuilder.newInstance()
                .setContentType("application/json")
                .setHeader("type", amqpRequest.getClass().getSimpleName())
                .build();

        Message message = MessageBuilder.withBody(objectMapper.writeValueAsString(amqpRequest).getBytes())
                .andProperties(properties)
                .build();

        Message response = template.sendAndReceive(wrapperQueueName, message);
        log.info("createProject: response = {}", response);
        return objectMapper.readValue(new String(response.getBody()), CreateProjectAmqpResponse.class);
    }

    public DeleteProjectAmqpResponse deleteProject(DeleteProjectAmqpRequest amqpRequest) throws JsonProcessingException {
        log.info("deleteProject: request = {}", amqpRequest);
        MessageProperties properties = MessagePropertiesBuilder.newInstance()
                .setContentType("application/json")
                .setHeader("type", amqpRequest.getClass().getSimpleName())
                .build();

        Message message = MessageBuilder.withBody(objectMapper.writeValueAsString(amqpRequest).getBytes())
                .andProperties(properties)
                .build();

        Message response = template.sendAndReceive(wrapperQueueName, message);
        log.info("deleteProject: response = {}", response);
        return objectMapper.readValue(new String(response.getBody()), DeleteProjectAmqpResponse.class);
    }

    public List<GetItemAmqpResponse> getItems(GetItemsAmqpRequest amqpRequest) throws JsonProcessingException {
        log.info("getProjectsByUserId: request = {}", amqpRequest);
        MessageProperties properties = MessagePropertiesBuilder.newInstance()
                .setContentType("application/json")
                .setHeader("type", amqpRequest.getClass().getSimpleName())
                .build();

        Message message = MessageBuilder.withBody(objectMapper.writeValueAsString(amqpRequest).getBytes())
                .andProperties(properties)
                .build();

        Message response = template.sendAndReceive(wrapperQueueName, message);
        log.info("getProjectsByUserId: response = {}", response);
        return Arrays.asList(objectMapper.readValue(new String(response.getBody()), GetItemAmqpResponse[].class));
    }

    public CreateItemAmqpResponse createItem(CreateItemAmqpRequest amqpRequest) throws JsonProcessingException {
        log.info("createItem: request = {}", amqpRequest);
        MessageProperties properties = MessagePropertiesBuilder.newInstance()
                .setContentType("application/json")
                .setHeader("type", amqpRequest.getClass().getSimpleName())
                .build();

        Message message = MessageBuilder.withBody(objectMapper.writeValueAsString(amqpRequest).getBytes())
                .andProperties(properties)
                .build();

        Message response = template.sendAndReceive(wrapperQueueName, message);
        log.info("createItem: response = {}", response);
        return objectMapper.readValue(new String(response.getBody()), CreateItemAmqpResponse.class);
    }

    public DeleteItemAmqpResponse deleteItem(DeleteItemAmqpRequest amqpRequest) throws JsonProcessingException {
        log.info("createItem: request = {}", amqpRequest);
        MessageProperties properties = MessagePropertiesBuilder.newInstance()
                .setContentType("application/json")
                .setHeader("type", amqpRequest.getClass().getSimpleName())
                .build();

        Message message = MessageBuilder.withBody(objectMapper.writeValueAsString(amqpRequest).getBytes())
                .andProperties(properties)
                .build();

        Message response = template.sendAndReceive(wrapperQueueName, message);
        log.info("createItem: response = {}", response);
        return objectMapper.readValue(new String(response.getBody()), DeleteItemAmqpResponse.class);
    }

    public GetItemAmqpResponse getItem(GetItemAmqpRequest amqpRequest) throws JsonProcessingException {
        log.info("getItem: request = {}", amqpRequest);
        MessageProperties properties = MessagePropertiesBuilder.newInstance()
                .setContentType("application/json")
                .setHeader("type", amqpRequest.getClass().getSimpleName())
                .build();

        Message message = MessageBuilder.withBody(objectMapper.writeValueAsString(amqpRequest).getBytes())
                .andProperties(properties)
                .build();

        Message response = template.sendAndReceive(wrapperQueueName, message);
        log.info("getItem: response = {}", response);
        return objectMapper.readValue(new String(response.getBody()), GetItemAmqpResponse.class);
    }
}
