package net.thumbtack.ptpb.handler.sessions;

import lombok.RequiredArgsConstructor;
import net.thumbtack.ptpb.db.session.SessionDao;
import net.thumbtack.ptpb.handler.common.EmptyResponse;
import net.thumbtack.ptpb.handler.common.Response;
import net.thumbtack.ptpb.handler.sessions.dto.requests.LoginUserRequest;
import net.thumbtack.ptpb.handler.sessions.dto.responses.LoginUserResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionsService {

    private final SessionDao sessionDao;

    public Response loginUser(LoginUserRequest request) {
        //TODO: not implemented
        return new LoginUserResponse();
    }

    public Response logoutUser(String cookie) {
        //TODO: not implemented

        return new EmptyResponse();
    }
}
