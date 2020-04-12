package net.thumbtack.ptpb.rabbitmq.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@SpringBootConfiguration
@EnableAutoConfiguration
//@EnableConfigurationProperties
@ComponentScan(basePackages = {
        "net.thumbtack.ptpb"
})
public class RabbitMqUserServiceTest {

    @Autowired
    private RabbitMqUserService rabbitMqUserService;


    @Test
    void testSyncUser() throws JsonProcessingException {
        SyncUserAmqpRequest request = SyncUserAmqpRequest.builder()
                .token("5f6e430cf393ae5db86773b5e79989fbef6a28d9")
                .build();

        SyncUserAmqpResponse response = rabbitMqUserService.syncUser(request);
        assertNotNull(response);
    }

}
