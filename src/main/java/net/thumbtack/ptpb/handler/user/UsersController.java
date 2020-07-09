package net.thumbtack.ptpb.handler.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.thumbtack.ptpb.common.PtpbException;
import net.thumbtack.ptpb.handler.common.Response;
import net.thumbtack.ptpb.handler.common.Types;
import net.thumbtack.ptpb.handler.user.dto.requests.DeleteUserRequest;
import net.thumbtack.ptpb.handler.user.dto.requests.RegisterUserRequest;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/users",
        //consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersController {

    private final UsersService usersService;

    @PostMapping
    public Response registerUser(@RequestBody @Valid RegisterUserRequest request,
                                 HttpServletResponse httpServletResponse) throws PtpbException, JsonProcessingException {
        log.info("[POST] register user: request={}", request);
        String uuid = UUID.randomUUID().toString();
        httpServletResponse.addCookie(new Cookie(Types.UUID, uuid));
        return usersService.registerUser(request, uuid);
    }

    @DeleteMapping(value = "{id}")
    public Response deleteUser(@RequestBody @Valid DeleteUserRequest request,
                               @PathVariable(value = "id") @Valid @NotEmpty String id,
                               @CookieValue(value = Types.UUID) String cookie
    ) throws PtpbException {
        log.info("[DELETE] delete user: id={}, request={}, uuid={}", id, request, cookie);
        return usersService.deleteUser(request, id, cookie);
    }
}
