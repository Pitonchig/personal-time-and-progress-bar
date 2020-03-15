package net.thumbtack.ptpb.handler.projects;

import net.thumbtack.ptpb.handler.common.Response;
import net.thumbtack.ptpb.handler.common.Types;
import net.thumbtack.ptpb.handler.projects.dto.requests.CreateProjectRequest;
import net.thumbtack.ptpb.handler.projects.dto.requests.CreateTaskRequest;
import net.thumbtack.ptpb.handler.projects.dto.responses.CreateTaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(value = "/api/projects",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectsController {

    private ProjectsService projectsService;

    @Autowired
    public ProjectsController(ProjectsService projectsService) {
        this.projectsService = projectsService;
    }

    //2.1
    @GetMapping()
    public List<? extends Response> getProjects(@Valid @NotEmpty @CookieValue(value = Types.UUID) String cookie) {
        return projectsService.getProjects(cookie);
    }

    //2.2
    @PostMapping()
    public Response createProject(@RequestBody @Valid CreateProjectRequest request,
                                  @CookieValue(value = Types.UUID) String cookie) {
        return projectsService.createProject(request, cookie);
    }

    //2.3
    @DeleteMapping(value = "{projectId}")
    public Response deleteProject(@PathVariable @NotNull @Min(1) Integer projectId,
                                  @CookieValue(value = Types.UUID) String cookie) {
        return projectsService.deleteProject(projectId, cookie);
    }

    //2.4
    @GetMapping(value = "{projectId}")
    public Response getProject(@PathVariable @NotNull @Min(1) Integer projectId,
                               @CookieValue(value = Types.UUID) String cookie) {
        return projectsService.getProject(projectId, cookie);
    }

    //3.1
    @GetMapping(value = "{projectId}/tasks")
    public List<? extends Response> getTasksByProjectId(@PathVariable @NotNull @Min(1) Integer projectId,
                                                        @CookieValue(value = Types.UUID) String cookie) {
        return projectsService.getTasksByProjectId(projectId, cookie);
    }

    //3.2
    @PostMapping(value = "{projectId}/tasks")
    public CreateTaskResponse createTask(@RequestBody @Valid CreateTaskRequest request,
                                         @PathVariable @NotNull @Min(1) Integer projectId,
                                         @CookieValue(value = Types.UUID) String cookie) {
        return projectsService.createTask(request, projectId, cookie);
    }

    //3.3
    @DeleteMapping(value = "tasks/{taskId}")
    public Response deleteTask(@PathVariable @NotNull @Min(1) Integer taskId,
                               @CookieValue(value = Types.UUID) String cookie) {
        return projectsService.deleteTask(taskId, cookie);
    }

    //3.4
    @GetMapping(value = "tasks/{taskId}")
    public Response getTask(@PathVariable @NotNull @Min(1) Integer taskId,
                            @CookieValue(value = Types.UUID) String cookie) {
        return projectsService.getTask(taskId, cookie);
    }
}
