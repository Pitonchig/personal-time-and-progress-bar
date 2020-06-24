package net.thumbtack.ptpb.rabbitmq.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RabbitMqUserServiceTest {

    @Autowired
    private RabbitMqUserService rabbitMqUserService;


//    @Test
//    void testSyncUser() throws JsonProcessingException, PtpbException {
//        SyncUserAmqpRequest request = SyncUserAmqpRequest.builder()
//                .token("5f6e430cf393ae5db86773b5e79989fbef6a28d9")
//                .build();
//
//        SyncUserAmqpResponse response = rabbitMqUserService.syncUser(request);
//        assertNotNull(response);
//    }

}
