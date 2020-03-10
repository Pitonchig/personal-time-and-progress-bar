package net.thumbtack.ptpb.handler.sessions;

import net.thumbtack.ptpb.handler.common.Response;
import net.thumbtack.ptpb.handler.sessions.dto.requests.LoginUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/sessions",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class SessionsController {

    private SessionsService sessionsService;

    @Autowired
    public SessionsController(SessionsService sessionService) {
        this.sessionsService = sessionService;
    }

    //1.3
    @PostMapping
    public Response loginUser(@RequestBody @Valid LoginUserRequest request) {
        return sessionsService.loginUser(request);
    }

    //1.4
    @DeleteMapping(value = "{id}")
    public Response logoutUser(@CookieValue String cookie) {
        return sessionsService.logoutUser(cookie);
    }
}
