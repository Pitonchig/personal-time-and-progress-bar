package net.thumbtack.ptpb.handler.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private String id;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "UTC")
    private ZonedDateTime start;
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "UTC")
    private ZonedDateTime finish;
    @JsonProperty("isCompleted")
    private boolean isCompleted;
}
