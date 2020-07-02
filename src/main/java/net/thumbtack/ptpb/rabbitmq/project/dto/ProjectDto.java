package net.thumbtack.ptpb.rabbitmq.project.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    private String id;
    private String name;
    @Singular
    List<ItemDto> items;
}
