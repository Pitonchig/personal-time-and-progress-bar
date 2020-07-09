package net.thumbtack.ptpb.handler.todoist;

import net.thumbtack.ptpb.db.user.Item;
import net.thumbtack.ptpb.db.user.Project;
import net.thumbtack.ptpb.rabbitmq.project.dto.ItemAmqpDto;
import net.thumbtack.ptpb.rabbitmq.project.dto.ProjectAmqpDto;

import java.util.LinkedList;
import java.util.List;

public class TodoistDtoConverter {
    public static List<ProjectAmqpDto> toProjectAmqpDtoList(List<Project> projects) {
        List<ProjectAmqpDto> dtoList = new LinkedList<>();
        for (Project project : projects) {
            dtoList.add(toProjectAmqpDto(project));
        }
        return dtoList;
    }

    public static ProjectAmqpDto toProjectAmqpDto(Project project) {
        List<ItemAmqpDto> itemDtoList = toItemAmqpDtoList(project.getItems());
        ProjectAmqpDto projectDto = ProjectAmqpDto.builder()
                .id(project.getId())
                .name(project.getProjectName())
                .items(itemDtoList)
                .build();
        return projectDto;
    }

    public static List<ItemAmqpDto> toItemAmqpDtoList(List<Item> items) {
        List<ItemAmqpDto> itemDtoList = new LinkedList<>();
        for (Item item : items) {
            itemDtoList.add(toItemAmqpDto(item));
        }
        return itemDtoList;
    }

    public static ItemAmqpDto toItemAmqpDto(Item item) {
        return ItemAmqpDto.builder()
                .id(item.getId())
                .content(item.getContent())
                .isCompleted(item.isCompleted())
                .start(item.getStart())
                .finish(item.getFinish())
                .build();
    }
}
