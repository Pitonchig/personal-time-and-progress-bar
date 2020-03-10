package net.thumbtack.ptpb.handler.projects.dto.requests;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CreateTaskRequest {
    @NotEmpty
    private String content;
}
