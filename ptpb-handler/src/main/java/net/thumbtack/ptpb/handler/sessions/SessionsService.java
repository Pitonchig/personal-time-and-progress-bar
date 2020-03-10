package net.thumbtack.ptpb.handler.sessions;

import net.thumbtack.ptpb.db.session.SessionDao;
import net.thumbtack.ptpb.handler.common.EmptyResponse;
import net.thumbtack.ptpb.handler.common.Response;
import net.thumbtack.ptpb.handler.sessions.dto.requests.LoginUserRequest;
import net.thumbtack.ptpb.handler.sessions.dto.responses.LoginUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionsService {

    private SessionDao sessionDao;

    @Autowired
    public SessionsService(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }

    public Response loginUser(LoginUserRequest request) {
        //TODO: not implemented
        return new LoginUserResponse();
    }

    public Response logoutUser(String cookie) {
        //TODO: not implemented

        return new EmptyResponse();
    }
}
