package net.thumbtack.ptpb.rabbitmq.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncUserAmqpResponse implements Serializable {
    private long id;
    private String name;
    private LocalDateTime registered;
}
