package net.thumbtack.ptpb.rabbitmq.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.thumbtack.ptpb.common.PtpbException;
import net.thumbtack.ptpb.rabbitmq.user.request.SyncUserAmqpRequest;
import net.thumbtack.ptpb.rabbitmq.user.response.SyncUserAmqpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RabbitMqUserServiceTest {

    @Autowired
    private RabbitMqUserService rabbitMqUserService;


    @Test
    void testSyncUser() throws JsonProcessingException, PtpbException {
        SyncUserAmqpRequest request = SyncUserAmqpRequest.builder()
                .token("5f6e430cf393ae5db86773b5e79989fbef6a28d9")
                .build();

        SyncUserAmqpResponse response = rabbitMqUserService.syncUser(request);
        assertNotNull(response);
    }

}
