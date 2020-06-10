package net.thumbtack.ptpb.handler.common;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Data
@Builder
public class ErrorResponse implements Response {
    @Singular
    List<PtpbError> errors;
}
