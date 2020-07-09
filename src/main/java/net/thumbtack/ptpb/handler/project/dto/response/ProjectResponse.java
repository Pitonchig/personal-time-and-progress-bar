package net.thumbtack.ptpb.handler.project.dto.response;

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
}
