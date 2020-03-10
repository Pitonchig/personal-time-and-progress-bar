package net.thumbtack.ptpb.handler.users.dto.requests;

import lombok.Data;
import net.thumbtack.ptpb.handler.common.Request;

import javax.validation.constraints.NotEmpty;

@Data
public class RegisterUserRequest implements Request {
    @NotEmpty
    private String login;
    @NotEmpty
    private String password;
    @NotEmpty
    private String token;
}
