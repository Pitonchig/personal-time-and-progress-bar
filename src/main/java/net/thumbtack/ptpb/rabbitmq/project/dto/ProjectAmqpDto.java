package net.thumbtack.ptpb.rabbitmq.project.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@JsonSerialize
public class ProjectAmqpDto {
    private String id;
    private String name;
    @Singular
    List<ItemAmqpDto> items;
}