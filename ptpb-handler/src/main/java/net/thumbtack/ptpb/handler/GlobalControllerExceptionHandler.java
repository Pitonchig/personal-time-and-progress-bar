package net.thumbtack.ptpb.handler;

import lombok.extern.slf4j.Slf4j;
import net.thumbtack.ptpb.handler.common.ErrorResponse;
import net.thumbtack.ptpb.handler.common.PtpbError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Slf4j
@ControllerAdvice
@EnableWebMvc
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse unknownErrorHandler(Exception ex) {
        log.error("Unknown error: ex={}", ex);
        return ErrorResponse.builder()
                .error( PtpbError.builder().message("DEBUG: internal server unknown error").build())        //FIXME: debug message
                .error( PtpbError.builder().message(ex.getMessage()).build())
                .build();
    }
}
