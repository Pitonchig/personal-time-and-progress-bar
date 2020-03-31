package net.thumbtack.ptpb.handler.project.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectDto {
    private long id;
    private String name;
}
