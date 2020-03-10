package net.thumbtack.ptpb.handler.users;

import net.thumbtack.ptpb.handler.common.Response;
import net.thumbtack.ptpb.handler.users.dto.requests.DeleteUserRequest;
import net.thumbtack.ptpb.handler.users.dto.requests.RegisterUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@Validated
@RequestMapping(value="/api/users",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersController {

    @Autowired
    UsersService usersService;

    //1.1
    @PostMapping
    public Response registerUser(@RequestBody @Valid RegisterUserRequest request) {
        return usersService.registerUser(request);
    }

    //1.2
    @DeleteMapping(value = "{id}")
    public Response deleteUser(@RequestBody @Valid DeleteUserRequest request,
                               @Min(1) int id,
                               @CookieValue String cookie) {
        return usersService.deleteUser(request, id, cookie);
    }
}
