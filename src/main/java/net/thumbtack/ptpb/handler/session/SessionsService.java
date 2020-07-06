package net.thumbtack.ptpb.handler.session;

import lombok.RequiredArgsConstructor;
import net.thumbtack.ptpb.db.services.Services;
import net.thumbtack.ptpb.db.session.Session;
import net.thumbtack.ptpb.db.session.SessionDao;
import net.thumbtack.ptpb.db.services.ServicesDao;
import net.thumbtack.ptpb.db.user.User;
import net.thumbtack.ptpb.db.user.UserDao;
import net.thumbtack.ptpb.handler.common.EmptyResponse;
import net.thumbtack.ptpb.common.ErrorCode;
import net.thumbtack.ptpb.common.PtpbException;
import net.thumbtack.ptpb.handler.session.dto.requests.LoginUserRequest;
import net.thumbtack.ptpb.handler.session.dto.responses.LoginUserResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static net.thumbtack.ptpb.common.ErrorCode.SESSION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SessionsService {

    private final SessionDao sessionDao;
    private final UserDao userDao;
    private final ServicesDao servicesDao;

    public LoginUserResponse loginUser(LoginUserRequest request, String uuid) throws PtpbException {
        Optional<User> result = userDao.getUserByName(request.getLogin());
        User user = result.orElseThrow(() -> new PtpbException(ErrorCode.USER_WRONG_NAME_OR_PASSWORD));
        Optional<Services> services = servicesDao.getServicesByUserUuid(user.getId());
        boolean isTodoistLinked = services.isPresent() && services.get().isTodoistLinked();

        Session session = Session.builder()
                .userId(user.getId())
                .uuid(uuid)
                .dateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
        sessionDao.insert(session);
        return LoginUserResponse.builder()
                .id(session.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .isTodoistLinked(isTodoistLinked)
                .build();
    }

    public EmptyResponse logoutUser(String uuid) throws PtpbException {
        Optional<Session> result = sessionDao.getSessionByUuid(uuid);
        Session session = result.orElseThrow(() -> new PtpbException(SESSION_NOT_FOUND));
        session.setExpired(true);
        sessionDao.update(session);
        return new EmptyResponse();
    }
}
