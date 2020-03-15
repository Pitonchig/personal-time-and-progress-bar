package net.thumbtack.ptpb.handler.projects.dto.responses;

import lombok.*;
import net.thumbtack.ptpb.handler.common.Response;
import net.thumbtack.ptpb.handler.projects.dto.TaskDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetProjectResponse implements Response {
    private int id;
    private String name;
    @Singular
    List<TaskDto> tasks;
}
