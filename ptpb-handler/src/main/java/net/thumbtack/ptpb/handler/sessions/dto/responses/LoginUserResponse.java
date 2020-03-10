package net.thumbtack.ptpb.handler.sessions.dto.responses;

import lombok.Data;
import net.thumbtack.ptpb.handler.common.Response;

@Data
public class LoginUserResponse implements Response {
    private int id;
}
