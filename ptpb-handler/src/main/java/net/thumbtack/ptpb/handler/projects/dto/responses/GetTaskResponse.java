package net.thumbtack.ptpb.handler.projects.dto.responses;

import lombok.Data;
import net.thumbtack.ptpb.handler.common.Response;

@Data
public class GetTaskResponse implements Response {
    private int id;
    private String content;
}
