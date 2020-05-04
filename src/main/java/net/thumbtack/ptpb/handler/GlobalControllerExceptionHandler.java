package net.thumbtack.ptpb.handler;

import lombok.extern.slf4j.Slf4j;
import net.thumbtack.ptpb.common.ErrorCode;
import net.thumbtack.ptpb.common.PtpbException;
import net.thumbtack.ptpb.handler.common.ErrorResponse;
import net.thumbtack.ptpb.handler.common.PtpbError;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

import static net.thumbtack.ptpb.common.ErrorCode.HTTP_MEDIA_TYPE;
import static net.thumbtack.ptpb.common.ErrorCode.SESSION_IS_MISSING;

@Slf4j
@ControllerAdvice
@EnableWebMvc
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(PtpbException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse ptpbExceptionHandler(PtpbException ex) {
        log.error("PtpbExcetion error: ", ex);
        List<ErrorCode> codeList = ex.getErrors();
        ErrorResponse.ErrorResponseBuilder errorResponseBuilder = ErrorResponse.builder();
        codeList.forEach(errorCode -> errorResponseBuilder.error(
                new PtpbError(errorCode.toString(), errorCode.getMessage())
        ));
        return errorResponseBuilder.build();
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse missingRequestCookieExceptionHandler(MissingRequestCookieException ex) {
        log.error("MissingRequestCookieException error: ", ex);
        return ErrorResponse.builder()
                .error(new PtpbError(SESSION_IS_MISSING.toString(), SESSION_IS_MISSING.getMessage()))
                .build();
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse httpMediaTypeNotSupportedExceptionHandler(HttpMediaTypeNotSupportedException ex) {
        log.error("HttpMediaTypeNotSupportedException error: ", ex);
        return ErrorResponse.builder()
                .error(new PtpbError(HTTP_MEDIA_TYPE.toString(), HTTP_MEDIA_TYPE.getMessage()))
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse unknownErrorHandler(Exception ex) {
        log.error("Unknown error: ", ex);
        return ErrorResponse.builder()
                .error(PtpbError.builder().message(ex.getMessage()).build())
                .build();
    }
}
