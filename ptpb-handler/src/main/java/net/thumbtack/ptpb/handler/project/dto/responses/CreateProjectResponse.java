package net.thumbtack.ptpb.handler.project.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.ptpb.handler.common.Response;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectResponse implements Response {
    private long id;
}
