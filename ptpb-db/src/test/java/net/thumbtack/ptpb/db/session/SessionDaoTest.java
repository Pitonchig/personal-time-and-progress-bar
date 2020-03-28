package net.thumbtack.ptpb.db.session;

import lombok.RequiredArgsConstructor;
import net.thumbtack.ptpb.db.DbConfiguration;
import net.thumbtack.ptpb.db.DbProperties;
import net.thumbtack.ptpb.db.user.User;
import net.thumbtack.ptpb.db.user.UserDao;
import net.thumbtack.ptpb.db.user.UserDaoImpl;
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
        UserDaoImpl.class,
        SessionDaoImpl.class
})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SessionDaoTest {

    private final UserDao userDao;
    private final SessionDao sessionDao;

    @BeforeEach
    void setup() {
        userDao.deleteAllUsers();
        sessionDao.deleteAllSessions();
    }

    @Test
    void testInsertAndGetSessionByUuid() {
        String uuid = UUID.randomUUID().toString();

        User user = User.builder()
                .name("userName")
                .password("password")
                .registered(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .token(UUID.randomUUID().toString())
                .build();
        userDao.insertUser(user);

        Session session = Session.builder()
                .uuid(uuid)
                .userName("User")
                .dateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
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
                    .userName(String.format("user-%d", i))
                    .dateTime(LocalDateTime.now())
                    .build();
            sessions.add(session);
        }
        sessions.forEach(sessionDao::insert);

        Session notInsertedSession = Session.builder()
                .uuid(UUID.randomUUID().toString())
                .userName("NotInsertedUser")
                .dateTime(LocalDateTime.now())
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
                .userName("User")
                .dateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
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
                    .userName(String.format("user-%d", i))
                    .dateTime(LocalDateTime.now())
                    .build();
            sessionDao.insert(session);
        }
        assertEquals(count, sessionDao.getAllSessions().size());
        sessionDao.deleteAllSessions();
        assertEquals(0, sessionDao.getAllSessions().size());
    }
}
