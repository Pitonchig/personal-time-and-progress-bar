package net.thumbtack.ptpb.handler.common;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNKNOWN_ERROR("", "Unknown error."),
    USER_NOT_FOUND("user id", "a user is not found"),
    USER_WRONG_NAME_OR_PASSWORD("user", "wrong user name or password"),
    USER_NAME_ALREADY_EXIST("user name", "user name is already exist"),
    USER_NOT_OWNER("user id", "a user is not owner of this resource"),
    SESSION_NOT_FOUND("cookie", "a session is not found"),
    PROJECT_NOT_FOUND("project id", "a project is not found"),
    ITEM_NOT_FOUND("item id", "a item is not found");

    private final String field;
    private final String message;

    ErrorCode(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
