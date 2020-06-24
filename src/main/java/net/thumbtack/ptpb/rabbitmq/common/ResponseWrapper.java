package net.thumbtack.ptpb.rabbitmq.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ResponseWrapper {
    private boolean isOk;
    private String data;
}
