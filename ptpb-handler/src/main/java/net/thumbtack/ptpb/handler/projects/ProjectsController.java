package net.thumbtack.ptpb.handler.projects;

import net.thumbtack.ptpb.handler.common.Response;
import net.thumbtack.ptpb.handler.projects.dto.requests.CreateProjectRequest;
import net.thumbtack.ptpb.handler.projects.dto.requests.CreateTaskRequest;
import net.thumbtack.ptpb.handler.projects.dto.responses.CreateTaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(value = "/api",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectsController {

    private ProjectsService projectsService;

    @Autowired
    public ProjectsController(ProjectsService projectsService) {
        this.projectsService = projectsService;
    }

    //2.1
    @GetMapping(value = "projects")
    public List<? extends Response> getProjectsList(@CookieValue String cookie) {
        return projectsService.getProjectsList(cookie);
    }

    //2.2
    @PostMapping(value = "projects")
    public Response createProject(@RequestBody @Valid CreateProjectRequest request,
                                  @CookieValue String cookie) {
        return projectsService.createProject(request, cookie);
    }

    //2.3
    @DeleteMapping(value = "projects/{projectId}")
    public Response deleteProject(@Min(1) int projectId,
                                  @CookieValue String cookie) {
        return projectsService.deleteProject(projectId, cookie);
    }

    //2.4
    @GetMapping(value = "projects/{projectId}")
    public Response getProject(@Min(1) int projectId,
                                   @CookieValue String cookie) {
        return projectsService.getProject(projectId, cookie);
    }

    //3.1
    @GetMapping(value = "projects/{projectId}/tasks")
    public List<? extends Response> getTasksByProjectId(@Min(1) int projectId,
                                                        @CookieValue String cookie) {
        return projectsService.getTasksByProjectId(projectId, cookie);
    }

    //3.2
    @PostMapping(value = "projects/{projectId}/tasks")
    public CreateTaskResponse createTask(@RequestBody @Valid CreateTaskRequest request,
                                         @Min(1) int projectId,
                                         @CookieValue String cookie) {
        return projectsService.createTask(request, projectId, cookie);
    }

    //3.3
    @DeleteMapping(value = "tasks/{taskId}")
    public Response deleteTask(@Min(1) int taskId,
                                 @CookieValue String cookie) {
        return projectsService.deleteTask(taskId, cookie);
    }

    //3.4
    @GetMapping(value = "tasks/{tasksId}")
    public Response getTask(@Min(1) int taskId,
                                   @CookieValue String cookie) {
        return projectsService.getTask(taskId, cookie);
    }
}
