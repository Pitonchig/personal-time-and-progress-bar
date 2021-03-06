package net.thumbtack.ptpb.rabbitmq.project.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectAmqpRequest {
    private long userId;
    private String name;
    private int color;
    private boolean isFavorite;
}
