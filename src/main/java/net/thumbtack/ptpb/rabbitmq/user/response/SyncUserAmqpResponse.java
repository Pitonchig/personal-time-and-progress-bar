package net.thumbtack.ptpb.rabbitmq.user.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncUserAmqpResponse {
    private long id;
    private String name;
    private LocalDateTime registered;
}
