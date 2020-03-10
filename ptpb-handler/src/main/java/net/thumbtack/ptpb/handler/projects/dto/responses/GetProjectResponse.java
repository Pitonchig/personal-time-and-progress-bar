package net.thumbtack.ptpb.handler.projects.dto.responses;

import lombok.Data;
import net.thumbtack.ptpb.handler.common.Response;
import net.thumbtack.ptpb.handler.projects.dto.TaskDto;

import java.util.List;

@Data
public class GetProjectResponse implements Response {
    private int id;
    private String name;
    List<TaskDto> tasks;
}
