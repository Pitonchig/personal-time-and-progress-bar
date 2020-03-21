package net.thumbtack.ptpb.handler.users;

import lombok.RequiredArgsConstructor;
import net.thumbtack.ptpb.handler.common.Response;
import net.thumbtack.ptpb.handler.users.dto.requests.DeleteUserRequest;
import net.thumbtack.ptpb.handler.users.dto.requests.RegisterUserRequest;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/users",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersController {

    private final UsersService usersService;

    @PostMapping
    public Response registerUser(@RequestBody @Valid RegisterUserRequest request) {
        return usersService.registerUser(request);
    }

    @DeleteMapping(value = "{id}")
    public Response deleteUser(@RequestBody @Valid DeleteUserRequest request,
                               @Min(1) int id,
                               @CookieValue String cookie) {
        return usersService.deleteUser(request, id, cookie);
    }
}
