package net.thumbtack.ptpb.handler.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.ptpb.handler.common.Response;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto implements Response {
    private long id;
    private long projectId;
    private long userId;
    private String content;
    private int priority;
    private LocalDateTime due;
    private boolean isCompleted;
}
