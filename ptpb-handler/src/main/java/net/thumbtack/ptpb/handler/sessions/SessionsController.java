package net.thumbtack.ptpb.handler.sessions;

import lombok.RequiredArgsConstructor;
import net.thumbtack.ptpb.handler.common.Response;
import net.thumbtack.ptpb.handler.sessions.dto.requests.LoginUserRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/sessions",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class SessionsController {

    private final SessionsService sessionsService;

    @PostMapping
    public Response loginUser(@RequestBody @Valid LoginUserRequest request) {
        return sessionsService.loginUser(request);
    }

    @DeleteMapping(value = "{id}")
    public Response logoutUser(@CookieValue String cookie) {
        return sessionsService.logoutUser(cookie);
    }
}
