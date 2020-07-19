package net.thumbtack.ptpb.handler.session;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.ptpb.common.PtpbException;
import net.thumbtack.ptpb.handler.common.EmptyResponse;
import net.thumbtack.ptpb.handler.common.Types;
import net.thumbtack.ptpb.handler.session.dto.requests.LoginUserRequest;
import net.thumbtack.ptpb.handler.session.dto.responses.LoginUserResponse;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@CrossOrigin
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/sessions",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class SessionsController {

    private final SessionsService sessionsService;

    @PostMapping
    public LoginUserResponse loginUser(@RequestBody @Valid LoginUserRequest request, HttpServletResponse httpServletResponse) throws PtpbException {
        log.info("[POST] loginUser: request={}", request);
        String uuid = UUID.randomUUID().toString();
        httpServletResponse.addCookie(new Cookie(Types.UUID, uuid));
        return sessionsService.loginUser(request, uuid);
    }

    @DeleteMapping()
    public EmptyResponse logoutUser(@CookieValue(value = Types.UUID) String cookie) throws PtpbException {
        log.info("[DELETE] logoutUser: cookie={}", cookie);
        return sessionsService.logoutUser(cookie);
    }
}
