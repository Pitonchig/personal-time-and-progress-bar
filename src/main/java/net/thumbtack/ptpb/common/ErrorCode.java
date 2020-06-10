package net.thumbtack.ptpb.common;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNKNOWN_ERROR("Unknown error."),
    HTTP_MEDIA_TYPE("Content type not supported"),
    USER_NOT_FOUND("a user is not found"),
    USER_WRONG_NAME_OR_PASSWORD("wrong user name or password"),
    USER_NAME_ALREADY_EXIST("user name is already exist"),
    USER_NOT_OWNER( "a user is not owner of this resource"),
    SESSION_NOT_FOUND("a session is not found"),
    SESSION_IS_MISSING("a cookie is missing"),
    PROJECT_NOT_FOUND( "project is not found"),
    ITEM_NOT_FOUND("item is not found"),
    WRAPPER_TIMEOUT("timeout while waiting for a ptpb-wrapper reply ");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
