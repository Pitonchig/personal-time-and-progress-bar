package net.thumbtack.ptpb.handler.session;

import lombok.RequiredArgsConstructor;
import net.thumbtack.ptpb.handler.common.PtpbException;
import net.thumbtack.ptpb.handler.common.Response;
import net.thumbtack.ptpb.handler.common.Types;
import net.thumbtack.ptpb.handler.session.dto.requests.LoginUserRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/sessions",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class SessionsController {

    private final SessionsService sessionsService;

    @PostMapping
    public Response loginUser(@RequestBody @Valid LoginUserRequest request, HttpServletResponse httpServletResponse) throws PtpbException {
        String uuid = UUID.randomUUID().toString();
        httpServletResponse.addCookie(new Cookie(Types.UUID, uuid));
        return sessionsService.loginUser(request, uuid);
    }

    @DeleteMapping()
    public Response logoutUser(@CookieValue(value = Types.UUID) String cookie) throws PtpbException {
        return sessionsService.logoutUser(cookie);
    }
}
