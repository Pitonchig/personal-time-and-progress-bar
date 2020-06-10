package net.thumbtack.ptpb.handler.session.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.ptpb.handler.common.Request;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserRequest implements Request {
    @NotBlank
    private String login;
    @NotBlank
    private String password;
}
