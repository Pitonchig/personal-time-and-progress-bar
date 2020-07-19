package net.thumbtack.ptpb.handler.project.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import net.thumbtack.ptpb.handler.project.dto.ItemDto;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectRequest {
    private String id;
    @NotEmpty
    private String name;
    @Singular
    List<ItemDto> items;
    @JsonProperty("isDeleted")
    private boolean isDeleted;
}
