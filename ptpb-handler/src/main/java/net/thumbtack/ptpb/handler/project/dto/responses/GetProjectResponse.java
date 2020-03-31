package net.thumbtack.ptpb.handler.project.dto.responses;

import lombok.*;
import net.thumbtack.ptpb.handler.common.Response;
import net.thumbtack.ptpb.handler.project.dto.ItemDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetProjectResponse implements Response {
    private long id;
    private String name;
    @Singular
    List<ItemDto> items;
    private int color;
    private boolean isFavorite;
}
