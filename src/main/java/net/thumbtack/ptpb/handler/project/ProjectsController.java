package net.thumbtack.ptpb.handler.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.ptpb.common.PtpbException;
import net.thumbtack.ptpb.handler.common.Response;
import net.thumbtack.ptpb.handler.common.Types;
import net.thumbtack.ptpb.handler.project.dto.request.UpdateProjectRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/projects",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectsController {

    private final ProjectsService projectsService;

    @GetMapping()
    public List<? extends Response> getUserProjects(@CookieValue(value = Types.UUID, required = false) String cookie) throws PtpbException, JsonProcessingException {
        return projectsService.getUserProjects(cookie);
    }

    @PutMapping()
    public List<? extends Response> updateUserProjects(@RequestBody @Valid List<UpdateProjectRequest> request,
                                                       @CookieValue(value = Types.UUID) String cookie) throws PtpbException {
        return projectsService.updateUserProjects(request, cookie);
    }

}
