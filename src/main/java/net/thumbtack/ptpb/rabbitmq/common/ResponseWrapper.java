package net.thumbtack.ptpb.rabbitmq.common;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
public class ResponseWrapper {
    private boolean isOk;
    private String data;
}
