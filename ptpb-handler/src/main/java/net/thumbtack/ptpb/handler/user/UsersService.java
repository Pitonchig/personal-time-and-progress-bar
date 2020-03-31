package net.thumbtack.ptpb.handler.user;

import lombok.RequiredArgsConstructor;
import net.thumbtack.ptpb.db.session.SessionDao;
import net.thumbtack.ptpb.db.user.User;
import net.thumbtack.ptpb.db.user.UserDao;
import net.thumbtack.ptpb.handler.common.EmptyResponse;
import net.thumbtack.ptpb.handler.common.PtpbException;
import net.thumbtack.ptpb.handler.user.dto.requests.DeleteUserRequest;
import net.thumbtack.ptpb.handler.user.dto.requests.RegisterUserRequest;
import net.thumbtack.ptpb.handler.user.dto.responses.RegisterUserResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static net.thumbtack.ptpb.handler.common.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UserDao userDao;
    private final SessionDao sessionDao;

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public RegisterUserResponse registerUser(RegisterUserRequest request) throws PtpbException {
        checkIsUserNameFree(request.getLogin());

        User user = User.builder()
                .id(System.nanoTime())
                .name(request.getLogin())
                .password(request.getPassword())
                .token(request.getToken())
                .registered(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();

        userDao.insertUser(user);
        return new RegisterUserResponse(user.getId());
    }

    public EmptyResponse deleteUser(DeleteUserRequest request, long userId, String uuid) throws PtpbException {
        checkSessionIsValid(uuid);
        Optional<User> result = userDao.getUserById(userId);
        User user = result.orElseThrow(() -> new PtpbException(USER_NOT_FOUND));

        if (user.getName() != request.getLogin()
                || user.getPassword() != request.getPassword()) {
            throw new PtpbException(USER_WRONG_NAME_OR_PASSWORD);
        }

        userDao.deleteUser(userId);
        return new EmptyResponse();
    }

    private void checkIsUserNameFree(String name) throws PtpbException {
        if (userDao.isRegistered(name)) {
            throw new PtpbException(USER_NAME_ALREADY_EXIST);
        }
    }

    private void checkSessionIsValid(String uuid) throws PtpbException {
        if (!sessionDao.isOnline(uuid)) {
            throw new PtpbException(SESSION_NOT_FOUND);
        }
    }

}
