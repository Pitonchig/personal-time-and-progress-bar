package net.thumbtack.ptpb.db.services;

import net.thumbtack.ptpb.db.DbConfiguration;
import net.thumbtack.ptpb.db.DbProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableConfigurationProperties
@ContextConfiguration(classes = {
        DbConfiguration.class,
        DbProperties.class,
        ServicesDao.class
})
public class TodoistDaoTest {

    @Autowired
    private ServicesDao servicesDao;

    @BeforeEach
    void setup() {
        servicesDao.deleteAllServices();
    }

    @Test
    void testInsert() {
        String userUuid = UUID.randomUUID().toString();

        assertFalse(servicesDao.getServicesByUserUuid(userUuid).isPresent());
        Services services = Services.builder()
                .userId(userUuid)
                .isTodoistLinked(true)
                .build();
        servicesDao.insertServices(services);
        Optional<Services> result = servicesDao.getServicesByUserUuid(userUuid);
        assertTrue(result.isPresent());
        assertEquals(services, result.get());
        assertTrue(result.get().isTodoistLinked());
    }

}
