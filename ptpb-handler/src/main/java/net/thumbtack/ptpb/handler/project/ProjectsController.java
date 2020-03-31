package net.thumbtack.ptpb.handler.project;

import lombok.RequiredArgsConstructor;
import net.thumbtack.ptpb.handler.common.PtpbException;
import net.thumbtack.ptpb.handler.common.Response;
import net.thumbtack.ptpb.handler.common.Types;
import net.thumbtack.ptpb.handler.project.dto.requests.CreateProjectRequest;
import net.thumbtack.ptpb.handler.project.dto.requests.CreateItemRequest;
import net.thumbtack.ptpb.handler.project.dto.responses.CreateItemResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/projects",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectsController {

    private final ProjectsService projectsService;

    @GetMapping()
    public List<? extends Response> getProjects(@Valid @NotEmpty @CookieValue(value = Types.UUID) String cookie) throws PtpbException {
        return projectsService.getUserProjects(cookie);
    }


    @PostMapping()
    public Response createProject(@RequestBody @Valid CreateProjectRequest request,
                                  @CookieValue(value = Types.UUID) String cookie) throws PtpbException {
        return projectsService.createProject(request, cookie);
    }


    @DeleteMapping(value = "{projectId}")
    public Response deleteProject(@PathVariable @NotNull @Min(1) long projectId,
                                  @CookieValue(value = Types.UUID) String cookie) throws PtpbException {
        return projectsService.deleteProject(projectId, cookie);
    }


    @GetMapping(value = "{projectId}")
    public Response getProject(@PathVariable @NotNull @Min(1) long projectId,
                               @CookieValue(value = Types.UUID) String cookie) throws PtpbException {
        return projectsService.getProject(projectId, cookie);
    }


    @GetMapping(value = "{projectId}/items")
    public List<? extends Response> getItemsByProjectId(@PathVariable @NotNull @Min(1) long projectId,
                                                        @CookieValue(value = Types.UUID) String cookie) throws PtpbException {
        return projectsService.getItemsByProjectId(projectId, cookie);
    }


    @PostMapping(value = "{projectId}/items")
    public CreateItemResponse createItem(@RequestBody @Valid CreateItemRequest request,
                                         @PathVariable @NotNull @Min(1) long projectId,
                                         @CookieValue(value = Types.UUID) String cookie) throws PtpbException {
        return projectsService.createItem(request, projectId, cookie);
    }


    @DeleteMapping(value = "items/{itemId}")
    public Response deleteItem(@PathVariable @NotNull @Min(1) long itemId,
                               @CookieValue(value = Types.UUID) String cookie) throws PtpbException {
        return projectsService.deleteItemById(itemId, cookie);
    }


    @GetMapping(value = "items/{itemId}")
    public Response getItem(@PathVariable @NotNull @Min(1) long itemId,
                            @CookieValue(value = Types.UUID) String cookie) throws PtpbException {
        return projectsService.getItemById(itemId, cookie);
    }
}
