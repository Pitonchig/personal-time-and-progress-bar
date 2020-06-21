package net.thumbtack.ptpb.db.todoist;

import net.thumbtack.ptpb.common.PtpbException;
import net.thumbtack.ptpb.db.DbConfiguration;
import net.thumbtack.ptpb.db.DbProperties;
import net.thumbtack.ptpb.db.session.SessionDao;
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
        TodoistDao.class
})
public class TodoistDaoTest {

    @Autowired
    private TodoistDao todoistDao;

    @BeforeEach
    void setup() {
        todoistDao.deleteAllTodoists();
    }

    @Test
    void testInsert() {
        String userUuid = UUID.randomUUID().toString();
        String token = UUID.randomUUID().toString();

        assertFalse(todoistDao.isTodoistLinked(userUuid));
        Todoist todoist = Todoist.builder()
                .userId(userUuid)
                .token(token)
                .build();
        todoistDao.insertTodoist(todoist);
        assertTrue(todoistDao.isTodoistLinked(userUuid));

        Optional<Todoist> result = todoistDao.getTodoistByUserUuid(userUuid);
        assertTrue(result.isPresent());
        assertEquals(todoist, result.get());
    }

}
