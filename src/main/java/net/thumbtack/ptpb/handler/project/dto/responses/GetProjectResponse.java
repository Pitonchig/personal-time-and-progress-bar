package net.thumbtack.ptpb.handler.project.dto.responses;

import lombok.*;
import net.thumbtack.ptpb.handler.common.Response;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetProjectResponse implements Response {
    private String id;
    private String name;
    @Singular
    List<GetItemResponse> items;
    private int color;
    private boolean isFavorite;
}
