package net.thumbtack.ptpb.handler.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.ptpb.common.PtpbException;
import net.thumbtack.ptpb.handler.common.Response;
import net.thumbtack.ptpb.handler.common.Types;
import net.thumbtack.ptpb.handler.project.dto.requests.CreateItemRequest;
import net.thumbtack.ptpb.handler.project.dto.requests.CreateProjectRequest;
import net.thumbtack.ptpb.handler.project.dto.requests.UpdateItemRequest;
import net.thumbtack.ptpb.handler.project.dto.requests.UpdateProjectRequest;
import net.thumbtack.ptpb.handler.project.dto.responses.CreateItemResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/projects",
        //consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectsController {

    private final ProjectsService projectsService;

    @GetMapping()
    public List<? extends Response> getProjects(@CookieValue(value = Types.UUID, required = false) String cookie) throws PtpbException, JsonProcessingException {
        return projectsService.getUserProjects(cookie);
    }


    @PostMapping()
    public Response createProject(@RequestBody @Valid CreateProjectRequest request,
                                  @CookieValue(value = Types.UUID) String cookie) throws PtpbException, JsonProcessingException {
        return projectsService.createProject(request, cookie);
    }


    @PutMapping(value = "{projectId}")
    public Response updateProject(@PathVariable @NotNull @Min(1) long projectId,
                                  @RequestBody @Valid UpdateProjectRequest request,
                                  @CookieValue(value = Types.UUID) String cookie) throws PtpbException, JsonProcessingException {
        return projectsService.updateProject(projectId, request, cookie);
    }


    @DeleteMapping(value = "{projectId}")
    public Response deleteProject(@PathVariable @NotNull @Min(1) long projectId,
                                  @CookieValue(value = Types.UUID) String cookie) throws PtpbException, JsonProcessingException {
        return projectsService.deleteProject(projectId, cookie);
    }


    @GetMapping(value = "{projectId}")
    public Response getProject(@PathVariable @NotNull @Min(1) long projectId,
                               @CookieValue(value = Types.UUID) String cookie) throws PtpbException, JsonProcessingException {
        return projectsService.getProject(projectId, cookie);
    }


//    @GetMapping(value = "{projectId}/items")
//    public List<? extends Response> getItemsByProjectId(@PathVariable @NotNull @Min(1) long projectId,
//                                                        @CookieValue(value = Types.UUID) String cookie) throws PtpbException, JsonProcessingException {
//        return projectsService.getItemsByProjectId(projectId, cookie);
//    }


    @PostMapping(value = "{projectId}/items")
    public CreateItemResponse createItem(@RequestBody @Valid CreateItemRequest request,
                                         @PathVariable @NotNull @Min(1) long projectId,
                                         @CookieValue(value = Types.UUID) String cookie) throws PtpbException, JsonProcessingException {
        log.info("createItem: projectId={}, request={}", projectId, request);
        return projectsService.createItem(request, projectId, cookie);
    }


    @PutMapping(value = "items/{itemId}")
    public Response updateItem(@PathVariable @NotNull @Min(1) long itemId,
                                  @RequestBody @Valid UpdateItemRequest request,
                                  @CookieValue(value = Types.UUID) String cookie) throws PtpbException, JsonProcessingException {
        log.info("updateItem: itemId={}, request={}", itemId, request);
        return projectsService.updateItem(itemId, request, cookie);
    }


    @DeleteMapping(value = "items/{itemId}")
    public Response deleteItem(@PathVariable @NotNull @Min(1) long itemId,
                               @CookieValue(value = Types.UUID) String cookie) throws PtpbException, JsonProcessingException {
        return projectsService.deleteItemById(itemId, cookie);
    }


//    @GetMapping(value = "items/{itemId}")
//    public Response getItem(@PathVariable @NotNull @Min(1) long itemId,
//                            @CookieValue(value = Types.UUID) String cookie) throws PtpbException, JsonProcessingException {
//        return projectsService.getItemById(itemId, cookie);
//    }

}
