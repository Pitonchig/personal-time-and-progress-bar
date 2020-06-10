package net.thumbtack.ptpb.handler.project.dto.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class GetItemResponse implements Response {
    private String id;
    private String projectId;
    private String userId;
    private String content;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime start;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime finish;
    @JsonProperty("isCompleted")
    private boolean isCompleted;
}
