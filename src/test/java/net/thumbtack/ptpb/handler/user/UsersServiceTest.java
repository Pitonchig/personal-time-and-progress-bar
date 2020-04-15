package net.thumbtack.ptpb.handler.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.thumbtack.ptpb.db.session.SessionDao;
import net.thumbtack.ptpb.db.user.User;
import net.thumbtack.ptpb.common.PtpbException;
import net.thumbtack.ptpb.db.user.UserDao;
import net.thumbtack.ptpb.handler.user.dto.requests.DeleteUserRequest;
import net.thumbtack.ptpb.handler.user.dto.requests.RegisterUserRequest;
import net.thumbtack.ptpb.handler.user.dto.responses.RegisterUserResponse;
import net.thumbtack.ptpb.rabbitmq.user.RabbitMqUserService;
import net.thumbtack.ptpb.rabbitmq.user.request.SyncUserAmqpRequest;
import net.thumbtack.ptpb.rabbitmq.user.response.SyncUserAmqpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static net.thumbtack.ptpb.common.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UsersServiceTest {
    private UsersService usersService;
    @MockBean
    private UserDao userDao;
    @MockBean
    private SessionDao sessionDao;
    @MockBean
    private RabbitMqUserService rabbitMqUserService;

    @BeforeEach
    void setup() {
        usersService = new UsersService(rabbitMqUserService, userDao, sessionDao);
    }

    @Test
    void testRegisterUser() throws PtpbException, JsonProcessingException {
        RegisterUserRequest request = RegisterUserRequest.builder()
                .login("login")
                .password("password")
                .token(UUID.randomUUID().toString())
                .build();
        when(userDao.isRegistered(request.getLogin())).thenReturn(false);
        SyncUserAmqpResponse amqpResponse = SyncUserAmqpResponse.builder()
                .id(System.nanoTime())
                .name(request.getLogin())
                .registered(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
        when(rabbitMqUserService.syncUser(any(SyncUserAmqpRequest.class))).thenReturn(amqpResponse);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        RegisterUserResponse response = usersService.registerUser(request);


        verify(userDao, times(1)).insertUser(userArgumentCaptor.capture());
        User user = userArgumentCaptor.getValue();
        assertNotNull(user);


        assertEquals(request.getLogin(), user.getName());
        assertEquals(request.getPassword(), user.getPassword());
        assertEquals(request.getToken(), user.getToken());
        assertNotNull(user.getRegistered());
    }

    @Test
    void testRegisterUserByAlreadyRegisteredName() {
        RegisterUserRequest request = RegisterUserRequest.builder()
                .login("login")
                .password("password")
                .token(UUID.randomUUID().toString())
                .build();
        when(userDao.isRegistered(request.getLogin())).thenReturn(true);

        try {
            usersService.registerUser(request);
            fail();
        } catch (PtpbException ex) {
            assertTrue(ex.getErrors().contains(USER_NAME_ALREADY_EXIST));
        } catch (JsonProcessingException e) {
            fail();
        }

        verify(userDao, times(0)).insertUser(any(User.class));
    }

    @Test
    void testDeleteUser() throws PtpbException {
        long id = System.nanoTime();
        String uuid = UUID.randomUUID().toString();
        DeleteUserRequest request = DeleteUserRequest.builder()
                .login("login")
                .password("password")
                .build();
        User user = User.builder()
                .id(id)
                .name(request.getLogin())
                .password(request.getPassword())
                .token(UUID.randomUUID().toString())
                .registered(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
        when(userDao.getUserById(id)).thenReturn(Optional.of(user));
        when(sessionDao.isOnline(uuid)).thenReturn(true);

        usersService.deleteUser(request, id, uuid);

        verify(userDao, times(1)).deleteUser(id);
    }

    @Test
    void testDeleteUserByWrongId() {
        long id = System.nanoTime();
        String uuid = UUID.randomUUID().toString();
        DeleteUserRequest request = DeleteUserRequest.builder()
                .login("login")
                .password("password")
                .build();
        when(userDao.getUserById(id)).thenReturn(Optional.empty());
        when(sessionDao.isOnline(uuid)).thenReturn(true);

        try {
            usersService.deleteUser(request, id, uuid);
            fail();
        } catch (PtpbException e) {
            assertTrue(e.getErrors().contains(USER_NOT_FOUND));
        }
        verify(userDao, times(0)).deleteUser(id);
    }

    @Test
    void testDeleteUserWithWrongLoginAndPassword() {
        long id = System.nanoTime();
        String uuid = UUID.randomUUID().toString();
        DeleteUserRequest request = DeleteUserRequest.builder()
                .login("login")
                .password("password")
                .build();
        User user = User.builder()
                .id(id)
                .name("wrong login")
                .password("wrong password")
                .token(UUID.randomUUID().toString())
                .registered(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
        when(userDao.getUserById(id)).thenReturn(Optional.of(user));
        when(sessionDao.isOnline(uuid)).thenReturn(true);

        try {
            usersService.deleteUser(request, id, uuid);
            fail();
        } catch (PtpbException e) {
            assertTrue(e.getErrors().contains(USER_WRONG_NAME_OR_PASSWORD));
        }

        verify(userDao, times(0)).deleteUser(id);
    }

}
