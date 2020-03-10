package net.thumbtack.ptpb.handler.common;

import lombok.Data;

@Data
public class PtpbError {
    private String code;
    private String field;
    private String message;
}
