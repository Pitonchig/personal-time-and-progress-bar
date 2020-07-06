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

    @BeforeEach
    void setup() {
        usersService = new UsersService(userDao, sessionDao);
    }

    @Test
    void testRegisterUser() throws PtpbException, JsonProcessingException {
        String uuid = UUID.randomUUID().toString();
        RegisterUserRequest request = RegisterUserRequest.builder()
                .login("login")
                .password("password")
                .email("email@gmail.com")
                .build();
        when(userDao.isRegistered(request.getLogin())).thenReturn(false);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        RegisterUserResponse response = usersService.registerUser(request, uuid);

        verify(userDao, times(1)).insertUser(userArgumentCaptor.capture());
        User user = userArgumentCaptor.getValue();
        assertNotNull(user);


        assertEquals(request.getLogin(), user.getName());
        assertEquals(request.getPassword(), user.getPassword());
        assertEquals(request.getEmail(), user.getEmail());
        assertNotNull(user.getRegistration());
    }

    @Test
    void testRegisterUserByAlreadyRegisteredName() {
        String uuid = UUID.randomUUID().toString();
        RegisterUserRequest request = RegisterUserRequest.builder()
                .login("login")
                .password("password")
                .email("email@gmail.com")
                .build();
        when(userDao.isRegistered(request.getLogin())).thenReturn(true);

        try {
            usersService.registerUser(request, uuid);
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
        String id = UUID.randomUUID().toString();
        String uuid = UUID.randomUUID().toString();
        DeleteUserRequest request = DeleteUserRequest.builder()
                .login("login")
                .password("password")
                .build();
        User user = User.builder()
                .id(id)
                .name(request.getLogin())
                .password(request.getPassword())
                .registration(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
        when(userDao.getUserById(id)).thenReturn(Optional.of(user));
        when(sessionDao.isOnline(uuid)).thenReturn(true);

        usersService.deleteUser(request, id, uuid);

        verify(userDao, times(1)).deleteUser(id);
    }

    @Test
    void testDeleteUserByWrongId() {
        String id = UUID.randomUUID().toString();
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
        String id = UUID.randomUUID().toString();
        String uuid = UUID.randomUUID().toString();
        DeleteUserRequest request = DeleteUserRequest.builder()
                .login("login")
                .password("password")
                .build();
        User user = User.builder()
                .id(id)
                .name("wrong login")
                .password("wrong password")
                .registration(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
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
