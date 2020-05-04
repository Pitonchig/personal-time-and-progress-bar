package net.thumbtack.ptpb.db.user;

import net.thumbtack.ptpb.common.PtpbException;
import net.thumbtack.ptpb.db.DbConfiguration;
import net.thumbtack.ptpb.db.DbProperties;
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
//@EnableConfigurationProperties
//@ContextConfiguration(classes = {
//        DbConfiguration.class,
//        DbProperties.class,
//        UserDaoImpl.class
//})
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @BeforeEach
    void setup() {
        userDao.deleteAllUsers();
    }

    @Test
    void testInsertAndGetUserByName() throws PtpbException {
        String userName = "User";
        User user = User.builder()
                .name(userName)
                .password("password")
                .registration(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .token(UUID.randomUUID().toString())
                .build();
        userDao.insertUser(user);

        Optional<User> result = userDao.getUserByName(userName);
        assertTrue(result.isPresent());
        assertEquals(result.get(), user);
    }

    @Test
    void testGetAllSessions() {
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            User user = User.builder()
                    .id(System.nanoTime())
                    .name(String.format("User-%d", i))
                    .password(String.format("password-%d", i))
                    .registration(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                    .token(UUID.randomUUID().toString())
                    .build();
            users.add(user);
        }
        users.forEach(userDao::insertUser);

        User notInsertedUser = User.builder()
                .id(System.nanoTime())
                .name("NotInsertedUser")
                .password("password")
                .registration(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
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
                .registration(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .token(UUID.randomUUID().toString())
                .build();

        assertFalse(userDao.isRegistered(user.getName()));
        userDao.insertUser(user);
        assertTrue(userDao.isRegistered(user.getName()));
    }

    @Test
    void testDeleteUser() {
        long id = System.nanoTime();
        User user = User.builder()
                .id(id)
                .name("User")
                .password("password")
                .registration(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .token(UUID.randomUUID().toString())
                .build();
        userDao.insertUser(user);
        assertTrue(userDao.isRegistered(user.getName()));

        userDao.deleteUser(id);
        assertFalse(userDao.isRegistered(user.getName()));
    }

    @Test
    void testDeleteAll() {
        int count = 10;
        assertEquals(0, userDao.getAllUsers().size());

        for (int i = 0; i < count; i++) {
            User user = User.builder()
                    .id(System.nanoTime())
                    .name(String.format("User-%d", i))
                    .password("password")
                    .registration(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                    .token(UUID.randomUUID().toString())
                    .build();
            userDao.insertUser(user);
        }
        assertEquals(count, userDao.getAllUsers().size());
        userDao.deleteAllUsers();
        assertEquals(0, userDao.getAllUsers().size());
    }

}