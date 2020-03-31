package net.thumbtack.ptpb.handler.user.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.ptpb.handler.common.Request;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequest implements Request {
    @NotEmpty
    private String login;
    @NotEmpty
    private String password;
    @NotEmpty
    private String token;
}
