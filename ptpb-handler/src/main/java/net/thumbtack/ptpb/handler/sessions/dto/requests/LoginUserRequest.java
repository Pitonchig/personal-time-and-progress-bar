package net.thumbtack.ptpb.handler.sessions.dto.requests;

import lombok.Data;
import net.thumbtack.ptpb.handler.common.Request;

@Data
public class LoginUserRequest implements Request {
    private String login;
    private String password;
}
