package net.thumbtack.ptpb.rabbitmq.project.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.ptpb.rabbitmq.project.dto.ProjectDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncProjectsAmqpRequest {
    private String userId;
    private boolean toTodoist;
    private boolean fromTodoist;
    private List<ProjectDto> projects;
}
