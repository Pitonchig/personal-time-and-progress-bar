package net.thumbtack.ptpb.db.user;

import lombok.RequiredArgsConstructor;
import net.thumbtack.ptpb.db.DbConfiguration;
import net.thumbtack.ptpb.db.DbProperties;
import net.thumbtack.ptpb.db.session.Session;
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
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@EnableConfigurationProperties
@ContextConfiguration(classes = {
        DbConfiguration.class,
        DbProperties.class,
        UserDaoImpl.class
})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserDaoTest {

    private final UserDao userDao;

    @BeforeEach
    void setup() {
        userDao.deleteAllUsers();
    }

    @Test
    void testInsertAndGetUserByName() {
        String userName = "User";
        User user = User.builder()
                .name(userName)
                .password("password")
                .registered(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .token(UUID.randomUUID().toString())
                .build();
        userDao.insertUser(user);

        Optional<User> result = userDao.getUserByName(userName);
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testGetAllSessions() {
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            User user = User.builder()
                    .name(String.format("User-%d", i))
                    .password(String.format("password-%d", i))
                    .registered(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                    .token(UUID.randomUUID().toString())
                    .build();
            users.add(user);
        }
        users.forEach(userDao::insertUser);

        User notInsertedUser = User.builder()
                .name("NotInsertedUser")
                .password("password")
                .registered(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .token(UUID.randomUUID().toString())
                .build();
        List<User> results = userDao.getAllUsers();

        assertAll(
                () -> assertTrue(users.containsAll(users)),
                () -> assertEquals(users.size(), results.size()),
                () -> assertFalse(results.contains(notInsertedUser))
        );
    }

    @Test
    void testIsRegistered() {
        User user = User.builder()
                .name("User")
                .password("password")
                .registered(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .token(UUID.randomUUID().toString())
                .build();

        assertFalse(userDao.isRegistered(user.getName()));
        userDao.insertUser(user);
        assertTrue(userDao.isRegistered(user.getName()));
    }

    @Test
    void testDeleteUser() {
        User user = User.builder()
                .name("User")
                .password("password")
                .registered(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .token(UUID.randomUUID().toString())
                .build();
        userDao.insertUser(user);
        assertTrue(userDao.isRegistered(user.getName()));

        userDao.deleteUser(user.getName());
        assertFalse(userDao.isRegistered(user.getName()));
    }

    @Test
    void testDeleteAll() {
        int count = 10;
        assertEquals(0, userDao.getAllUsers().size());

        for (int i = 0; i < count; i++) {
            User user = User.builder()
                    .name(String.format("User-%d", i))
                    .password("password")
                    .registered(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                    .token(UUID.randomUUID().toString())
                    .build();
            userDao.insertUser(user);
        }
        assertEquals(count, userDao.getAllUsers().size());
        userDao.deleteAllUsers();
        assertEquals(0, userDao.getAllUsers().size());
    }

}