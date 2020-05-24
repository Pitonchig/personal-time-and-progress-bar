package net.thumbtack.ptpb.handler.project.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateItemRequest {
    @NotEmpty
    private String content;
    @JsonProperty("isCompleted")
    private boolean isCompleted;
    private LocalDateTime start;
    private LocalDateTime finish;
}
