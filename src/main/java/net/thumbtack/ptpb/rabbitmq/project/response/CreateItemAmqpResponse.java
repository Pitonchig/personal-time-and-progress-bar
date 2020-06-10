package net.thumbtack.ptpb.rabbitmq.project.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemAmqpResponse {
    private long itemId;
}
