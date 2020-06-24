package net.thumbtack.ptpb.handler.todoist.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.ptpb.handler.common.Request;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncProjectsRequest implements Request {
    private boolean from;
    private boolean to;
}
