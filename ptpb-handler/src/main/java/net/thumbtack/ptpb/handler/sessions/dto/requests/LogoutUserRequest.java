package net.thumbtack.ptpb.handler.sessions.dto.requests;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import net.thumbtack.ptpb.handler.common.Request;

@Data
@JsonSerialize
public class LogoutUserRequest implements Request {


}
