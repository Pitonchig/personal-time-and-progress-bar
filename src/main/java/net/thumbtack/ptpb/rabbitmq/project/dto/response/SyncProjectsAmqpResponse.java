package net.thumbtack.ptpb.rabbitmq.project.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import net.thumbtack.ptpb.rabbitmq.project.dto.ProjectAmqpDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
public class SyncProjectsAmqpResponse {
    private String userId;
    private boolean toTodoist;
    private boolean fromTodoist;
    @Singular
    private List<ProjectAmqpDto> projects;
}
