package net.thumbtack.ptpb.handler.common;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNKNOWN_ERROR("", "Unknown error.");

    private final String field;
    private final String message;

    ErrorCode(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
