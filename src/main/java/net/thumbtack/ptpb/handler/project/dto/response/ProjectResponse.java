package net.thumbtack.ptpb.handler.project.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import net.thumbtack.ptpb.handler.common.Response;
import net.thumbtack.ptpb.handler.project.dto.ItemDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse implements Response {
    private String id;
    private String name;
    @Singular
    List<ItemDto> items;
    @JsonProperty("isDeleted")
    private boolean isDeleted;
}
