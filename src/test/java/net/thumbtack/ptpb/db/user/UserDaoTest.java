package net.thumbtack.ptpb.db.user;

import net.thumbtack.ptpb.common.PtpbException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @BeforeEach
    void setup() {
        userDao.deleteAllUsers();
    }

    @Test
    void testInsertAndGetUserById() throws PtpbException {
        String uuid = UUID.randomUUID().toString();
        User user = User.builder()
                .id(uuid)
                .name("userName")
                .password("password")
                .registration(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
        userDao.insertUser(user);

        Optional<User> result = userDao.getUserById(uuid);
        assertTrue(result.isPresent());
        assertEquals(result.get(), user);
    }

    @Test
    void testInsertAndGetUserByName() throws PtpbException {
        String userName = "User";
        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .name(userName)
                .password("password")
                .registration(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
        userDao.insertUser(user);

        Optional<User> result = userDao.getUserByName(userName);
        assertTrue(result.isPresent());
        assertEquals(result.get(), user);
    }

    @Test
    void testGetAllUsers() {
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            User user = User.builder()
                    .id(UUID.randomUUID().toString())
                    .name(String.format("User-%d", i))
                    .password(String.format("password-%d", i))
                    .registration(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                    .build();
            users.add(user);
        }
        users.forEach(userDao::insertUser);

        User notInsertedUser = User.builder()
                .id(UUID.randomUUID().toString())
                .name("NotInsertedUser")
                .password("password")
                .registration(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
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
                .id(UUID.randomUUID().toString())
                .name("User")
                .password("password")
                .registration(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();

        assertFalse(userDao.isRegistered(user.getName()));
        userDao.insertUser(user);
        assertTrue(userDao.isRegistered(user.getName()));
    }

    @Test
    void testDeleteUser() {
        String id = UUID.randomUUID().toString();
        User user = User.builder()
                .id(id)
                .name("User")
                .password("password")
                .registration(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
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
                    .id(UUID.randomUUID().toString())
                    .name(String.format("User-%d", i))
                    .password("password")
                    .registration(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                    .build();
            userDao.insertUser(user);
        }
        assertEquals(count, userDao.getAllUsers().size());
        userDao.deleteAllUsers();
        assertEquals(0, userDao.getAllUsers().size());
    }

    @ParameterizedTest
    @CsvSource({
            "Peter, peterpass, Peter, peterpass, true",
            "Peter, peterpass, Peter1, peterpass, false",
            "Peter, peterpass, Pete, peterpass, false",
            "Peter, peterpass, Peter, peterpass1, false",
            "Peter, peterpass, Peter, peterpas, false",
            "Peter, peterpass, peterpass, Peter, false",
            "Peter, peterpass, peter, peterpass, false",
            "Peter, peterpass, Peter, peTerpass, false",
            "Peter, peterpass, Peter,  , false"
    })
    void testInsertAndGetUserByNameAndPassword(String registrationLogin, String registrationPassword,
                                               String login, String password, boolean expected) throws PtpbException {
        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .name(registrationLogin)
                .password(registrationPassword)
                .registration(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
        userDao.insertUser(user);

        Optional<User> result = userDao.getUserByNameAndPassword(login, password);
        assertEquals(expected, result.isPresent());

        if (expected && result.isPresent()) {
            User resultUser = result.get();
            assertEquals(user.getId(), resultUser.getId());
            assertEquals(user.getName(), resultUser.getName());
            assertEquals(user.getPassword(), resultUser.getPassword());
        }
    }

}