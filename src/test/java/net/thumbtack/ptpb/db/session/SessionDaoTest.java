package net.thumbtack.ptpb.db.session;

import net.thumbtack.ptpb.db.DbConfiguration;
import net.thumbtack.ptpb.db.DbProperties;
import net.thumbtack.ptpb.db.user.User;
import net.thumbtack.ptpb.db.user.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableConfigurationProperties
@ContextConfiguration(classes = {
        DbConfiguration.class,
        DbProperties.class,
        UserDao.class,
        SessionDao.class
})
public class SessionDaoTest {

    @Autowired
    private UserDao userDao;
    @Autowired
    private SessionDao sessionDao;

    @BeforeEach
    void setup() {
        userDao.deleteAllUsers();
        sessionDao.deleteAllSessions();
    }

    @Test
    void testInsertAndGetSessionByUuid() {
        String uuid = UUID.randomUUID().toString();

        User user = User.builder()
                .id(System.nanoTime())
                .name("userName")
                .password("password")
                .registration(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .token(UUID.randomUUID().toString())
                .build();
        userDao.insertUser(user);

        Session session = Session.builder()
                .uuid(uuid)
                .userId(user.getId())
                .dateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .isExpired(false)
                .build();
        sessionDao.insert(session);

        Optional<Session> result = sessionDao.getSessionByUuid(uuid);
        assertTrue(result.isPresent());
        assertEquals(session, result.get());
    }

    @Test
    void testGetAllSessions() {
        List<Session> sessions = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            Session session = Session.builder()
                    .uuid(UUID.randomUUID().toString())
                    .userId(System.nanoTime())
                    .dateTime(LocalDateTime.now())
                    .isExpired(false)
                    .build();
            sessions.add(session);
        }
        sessions.forEach(sessionDao::insert);

        Session notInsertedSession = Session.builder()
                .uuid(UUID.randomUUID().toString())
                .userId(System.nanoTime())
                .dateTime(LocalDateTime.now())
                .isExpired(false)
                .build();
        List<Session> results = sessionDao.getAllSessions();

        assertAll(
                () -> assertTrue(sessions.containsAll(sessions)),
                () -> assertEquals(sessions.size(), results.size()),
                () -> assertFalse(results.contains(notInsertedSession))
        );
    }

    @Test
    void testIsOnline() {
        String uuid = UUID.randomUUID().toString();
        assertFalse(sessionDao.isOnline(uuid));

        Session session = Session.builder()
                .uuid(uuid)
                .userId(System.nanoTime())
                .dateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .isExpired(false)
                .build();
        sessionDao.insert(session);
        assertTrue(sessionDao.isOnline(uuid));
    }

    @Test
    void testDeleteAll() {
        int count = 10;
        assertEquals(0, sessionDao.getAllSessions().size());

        for (int i = 0; i < count; i++) {
            Session session = Session.builder()
                    .uuid(UUID.randomUUID().toString())
                    .userId(System.nanoTime())
                    .dateTime(LocalDateTime.now())
                    .isExpired(false)
                    .build();
            sessionDao.insert(session);
        }
        assertEquals(count, sessionDao.getAllSessions().size());
        sessionDao.deleteAllSessions();
        assertEquals(0, sessionDao.getAllSessions().size());
    }
}
