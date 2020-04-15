package net.thumbtack.ptpb.handler.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PtpbError {
    private String code;
    private String field;
    private String message;
}
