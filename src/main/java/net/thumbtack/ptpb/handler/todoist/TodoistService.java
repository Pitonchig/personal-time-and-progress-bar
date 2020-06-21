package net.thumbtack.ptpb.handler.todoist;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.ptpb.common.PtpbException;
import net.thumbtack.ptpb.db.session.Session;
import net.thumbtack.ptpb.db.session.SessionDao;
import net.thumbtack.ptpb.db.todoist.Todoist;
import net.thumbtack.ptpb.db.todoist.TodoistDao;
import net.thumbtack.ptpb.handler.todoist.dto.request.UpdateTodoistTokenRequest;
import net.thumbtack.ptpb.handler.todoist.dto.response.UpdateTodoistTokenResponse;
import org.springframework.stereotype.Service;

import static net.thumbtack.ptpb.common.ErrorCode.SESSION_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoistService {
    private final TodoistDao todoistDao;
    private final SessionDao sessionDao;

    public UpdateTodoistTokenResponse updateTodoistToken(UpdateTodoistTokenRequest request, String cookie) throws PtpbException {
        Session session = getSessionByUuid(cookie);
        boolean isTokenValid = false;

        if(request.getToken() == null || request.getToken().isBlank()) {
            todoistDao.delete(session.getUserId());
            log.info("token deleted: user={}", session.getUserId());
        } else {
            Todoist todoist = Todoist.builder()
                    .userId(session.getUserId())
                    .token(request.getToken())
                    .build();
            todoistDao.insertTodoist(todoist);
            log.info("token updated: user={} token={}", session.getUserId(), request.getToken());
            isTokenValid = true; //TODO: todoist.com token validation
        }

        return UpdateTodoistTokenResponse.builder()
                .isTodoistLinked(isTokenValid)
                .build();
    }

    private Session getSessionByUuid(String uuid) throws PtpbException {
        return sessionDao.getSessionByUuid(uuid).orElseThrow(() -> new PtpbException(SESSION_NOT_FOUND));
    }
}
