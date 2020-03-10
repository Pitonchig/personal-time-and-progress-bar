package net.thumbtack.ptpb.handler.projects.dto.requests;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class CreateProjectRequest {
    @NotEmpty
    private String name;
}
