package net.thumbtack.ptpb.handler.project;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.ptpb.db.session.Session;
import net.thumbtack.ptpb.db.session.SessionDao;
import net.thumbtack.ptpb.db.user.Project;
import net.thumbtack.ptpb.db.user.User;
import net.thumbtack.ptpb.db.user.UserDao;
import net.thumbtack.ptpb.common.PtpbException;
import net.thumbtack.ptpb.handler.common.Response;
import net.thumbtack.ptpb.handler.project.dto.request.UpdateProjectRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import static net.thumbtack.ptpb.common.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectsService {
    private final SessionDao sessionDao;
    private final UserDao userDao;

    public List<? extends Response> getUserProjects(String cookie) throws PtpbException {
        log.info("get user projects request: cookie = {}", cookie);
        User user = getUserBySessionUuid(cookie);

        List<? extends Response> responses = ProjectDtoConverter.toProjectResponseList(user.getProjects());
        log.info("get user projects response: projects = {}", responses);
        return responses;
    }

    public List<? extends Response> updateUserProjects(List<UpdateProjectRequest> request, String cookie) throws PtpbException {
        log.info("update user projects request: cookie={}, projects = {}", cookie, request);
        User user = getUserBySessionUuid(cookie);

        List<Project> projects = ProjectDtoConverter.fromUpdateProjectRequestList(request);
        user.setProjects(projects);
        userDao.insertUser(user);

        List<? extends Response> responses = ProjectDtoConverter.toProjectResponseList(user.getProjects());
        log.info("update user projects response: projects = {}", responses);
        return responses;
    }

    private Session getSessionByUuid(String uuid) throws PtpbException {
        return sessionDao.getSessionByUuid(uuid)
                .orElseThrow(() -> new PtpbException(SESSION_NOT_FOUND));
    }

    private User getUserBySessionUuid(String uuid) throws PtpbException {
        Session session = getSessionByUuid(uuid);
        return userDao.getUserById(session.getUserId())
                .orElseThrow(() -> new PtpbException(USER_NOT_FOUND));
    }
}
