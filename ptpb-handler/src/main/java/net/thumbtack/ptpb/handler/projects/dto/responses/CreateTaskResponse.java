package net.thumbtack.ptpb.handler.projects.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.ptpb.handler.common.Response;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskResponse implements Response {
    private int id;
}
