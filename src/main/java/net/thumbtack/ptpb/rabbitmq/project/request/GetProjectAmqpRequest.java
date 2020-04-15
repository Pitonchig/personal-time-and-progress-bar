package net.thumbtack.ptpb.rabbitmq.project.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetProjectAmqpRequest {
    private long projectId;
    private long userId;
}
