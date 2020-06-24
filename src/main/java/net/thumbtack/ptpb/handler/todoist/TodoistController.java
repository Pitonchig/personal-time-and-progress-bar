package net.thumbtack.ptpb.handler.todoist;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.ptpb.common.PtpbException;
import net.thumbtack.ptpb.handler.common.Response;
import net.thumbtack.ptpb.handler.common.Types;
import net.thumbtack.ptpb.handler.todoist.dto.request.SyncProjectsRequest;
import net.thumbtack.ptpb.handler.todoist.dto.request.UpdateTodoistTokenRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/users",
        //consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class TodoistController {
    private final TodoistService todoistService;

    @PutMapping(value = "tokens")
    public Response updateTodoistToken(@RequestBody @Valid UpdateTodoistTokenRequest request,
                                  @CookieValue(value = Types.UUID) String cookie) throws PtpbException, JsonProcessingException {
        log.info("[PUT] updateTodoistToken: request={}", request);
        return todoistService.updateTodoistToken(request, cookie);
    }

    @PutMapping(value = "services")
    public Response syncProjects(@RequestBody @Valid SyncProjectsRequest request,
                                       @CookieValue(value = Types.UUID) String cookie) throws PtpbException, JsonProcessingException {
        log.info("[PUT] syncProjects: request={}", request);
        return todoistService.syncProjects(request, cookie);
    }

}
