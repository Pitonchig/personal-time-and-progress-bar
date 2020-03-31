package net.thumbtack.ptpb.handler.session;

import lombok.RequiredArgsConstructor;
import net.thumbtack.ptpb.db.session.Session;
import net.thumbtack.ptpb.db.session.SessionDao;
import net.thumbtack.ptpb.db.user.User;
import net.thumbtack.ptpb.db.user.UserDao;
import net.thumbtack.ptpb.handler.common.EmptyResponse;
import net.thumbtack.ptpb.handler.common.ErrorCode;
import net.thumbtack.ptpb.handler.common.PtpbException;
import net.thumbtack.ptpb.handler.session.dto.requests.LoginUserRequest;
import net.thumbtack.ptpb.handler.session.dto.responses.LoginUserResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static net.thumbtack.ptpb.handler.common.ErrorCode.SESSION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SessionsService {

    private final SessionDao sessionDao;
    private final UserDao userDao;

    public LoginUserResponse loginUser(LoginUserRequest request, String uuid) throws PtpbException {
        Optional<User> result = userDao.getUserByName(request.getLogin());
        User user = result.orElseThrow(() -> new PtpbException(ErrorCode.USER_WRONG_NAME_OR_PASSWORD));

        Session session = Session.builder()
                .userId(user.getId())
                .uuid(uuid)
                .dateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
        sessionDao.insert(session);
        return new LoginUserResponse(session.getUserId());
    }

    public EmptyResponse logoutUser(String uuid) throws PtpbException {
        Optional<Session> result = sessionDao.getSessionByUuid(uuid);
        Session session = result.orElseThrow(() -> new PtpbException(SESSION_NOT_FOUND));
        session.setExpired(true);
        sessionDao.update(session);
        return new EmptyResponse();
    }
}
