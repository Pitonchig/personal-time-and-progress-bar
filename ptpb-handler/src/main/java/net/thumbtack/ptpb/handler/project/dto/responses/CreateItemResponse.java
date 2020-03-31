package net.thumbtack.ptpb.handler.project.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.ptpb.handler.common.Response;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemResponse implements Response {
    private long id;
}
