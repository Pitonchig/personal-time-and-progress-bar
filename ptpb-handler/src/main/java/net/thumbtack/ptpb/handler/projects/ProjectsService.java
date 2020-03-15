package net.thumbtack.ptpb.handler.projects;

import net.thumbtack.ptpb.db.project.ProjectDao;
import net.thumbtack.ptpb.handler.common.EmptyResponse;
import net.thumbtack.ptpb.handler.common.Response;
import net.thumbtack.ptpb.handler.projects.dto.requests.CreateProjectRequest;
import net.thumbtack.ptpb.handler.projects.dto.requests.CreateTaskRequest;
import net.thumbtack.ptpb.handler.projects.dto.responses.CreateProjectResponse;
import net.thumbtack.ptpb.handler.projects.dto.responses.CreateTaskResponse;
import net.thumbtack.ptpb.handler.projects.dto.responses.GetProjectResponse;
import net.thumbtack.ptpb.handler.projects.dto.responses.GetTaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ProjectsService {

    private ProjectDao projectDao;

    @Autowired
    public ProjectsService(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    public List<? extends Response> getProjects(String cookie) {
        //TODO: not implemented
        return new LinkedList<>();
    }

    public Response createProject(CreateProjectRequest request, String cookie) {
        //TODO: not implemented
        return new CreateProjectResponse(0);
    }

    public Response deleteProject(int id, String cookie) {
        //TODO: not implemented
        return new EmptyResponse();
    }

    public Response getProject(int id, String cookie) {
        //TODO: not implemented
        return new GetProjectResponse();
    }

    public List<? extends Response> getTasksByProjectId(int projectId, String cookie) {
        //TODO: not implemented
        return new LinkedList<>();
    }

    public CreateTaskResponse createTask(CreateTaskRequest request, int projectId, String cookie) {
        //TODO: not implemented
        return new CreateTaskResponse();
    }

    public Response deleteTask(int taskId, String cookie) {
        //TODO: not implemented
        return new EmptyResponse();
    }

    public Response getTask(int taskId, String cookie) {
        //TODO: not implemented
        return new GetTaskResponse();
    }
}
