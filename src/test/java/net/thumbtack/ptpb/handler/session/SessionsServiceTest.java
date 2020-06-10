package net.thumbtack.ptpb.handler.session;

import net.thumbtack.ptpb.db.session.Session;
import net.thumbtack.ptpb.db.session.SessionDao;
import net.thumbtack.ptpb.db.user.User;
import net.thumbtack.ptpb.db.user.UserDao;
import net.thumbtack.ptpb.handler.common.EmptyResponse;
import net.thumbtack.ptpb.common.PtpbException;
import net.thumbtack.ptpb.handler.session.dto.requests.LoginUserRequest;
import net.thumbtack.ptpb.handler.session.dto.responses.LoginUserResponse;
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

import static net.thumbtack.ptpb.common.ErrorCode.USER_WRONG_NAME_OR_PASSWORD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class SessionsServiceTest {
    private SessionsService sessionsService;

    @MockBean
    private SessionDao sessionDao;

    @MockBean
    private UserDao userDao;

    @BeforeEach
    void setup() {
        sessionsService = new SessionsService(sessionDao, userDao);
    }

    @Test
    void testLoginUser() throws PtpbException {
        String uuid = UUID.randomUUID().toString();
        LoginUserRequest request = LoginUserRequest.builder()
                .login("login")
                .password("pass")
                .build();
        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .name(request.getLogin())
                .password(request.getPassword())
                .token(UUID.randomUUID().toString())
                .registration(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
        when(userDao.getUserByName(request.getLogin())).thenReturn(Optional.of(user));
        LoginUserResponse response = sessionsService.loginUser(request, uuid);
        assertNotNull(response);
        assertEquals(user.getId(), response.getId());

        ArgumentCaptor<Session> captor = ArgumentCaptor.forClass(Session.class);
        verify(sessionDao, times(1)).insert(captor.capture());
        Session capturedSession = captor.getValue();
        assertNotNull(capturedSession);
        assertEquals(uuid, capturedSession.getUuid());
        assertEquals(user.getId(), capturedSession.getUserId());
        assertNotNull(capturedSession.getDateTime());
        assertFalse(capturedSession.isExpired());
    }


    @Test
    void testLogoutUser() throws PtpbException {
        String uuid = UUID.randomUUID().toString();
        Session session = Session.builder()
                .uuid(uuid)
                .userId(UUID.randomUUID().toString())
                .dateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .isExpired(false)
                .build();
        when(sessionDao.getSessionByUuid(uuid)).thenReturn(Optional.of(session));
        EmptyResponse response = sessionsService.logoutUser(uuid);
        assertNotNull(response);
        verify(sessionDao, times(1)).getSessionByUuid(uuid);

        ArgumentCaptor<Session> sessionArgumentCaptor = ArgumentCaptor.forClass(Session.class);
        verify(sessionDao, times(1)).update(sessionArgumentCaptor.capture());

        Session capturedSession = sessionArgumentCaptor.getValue();
        assertTrue(capturedSession.isExpired());
    }

    @Test
    void testLoginUserWithWrongNameOrPassword() throws PtpbException {
        String uuid = UUID.randomUUID().toString();
        LoginUserRequest request = LoginUserRequest.builder()
                .login("login")
                .password("pass")
                .build();
        when(userDao.getUserByName(request.getLogin())).thenReturn(Optional.empty());

        try {
            sessionsService.loginUser(request, uuid);
        } catch (PtpbException e) {
            e.getErrors().contains(USER_WRONG_NAME_OR_PASSWORD);
        }
        verify(sessionDao, times(0)).insert(any(Session.class));
    }


}
