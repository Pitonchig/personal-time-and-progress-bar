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
        if (projects == null) {
            return dtoList;
        }

        for (Project project : projects) {
            dtoList.add(toProjectAmqpDto(project));
        }
        return dtoList;
    }

    public static ProjectAmqpDto toProjectAmqpDto(Project project) {
        if (project == null) {
            return null;
        }

        List<ItemAmqpDto> itemDtoList = toItemAmqpDtoList(project.getItems());
        ProjectAmqpDto projectDto = ProjectAmqpDto.builder()
                .id(project.getId())
                .name(project.getProjectName())
                .isDeleted(project.isDeleted())
                .items(itemDtoList)
                .build();
        return projectDto;
    }

    public static List<ItemAmqpDto> toItemAmqpDtoList(List<Item> items) {
        List<ItemAmqpDto> itemDtoList = new LinkedList<>();
        if (items == null) {
            return itemDtoList;
        }

        for (Item item : items) {
            itemDtoList.add(toItemAmqpDto(item));
        }
        return itemDtoList;
    }

    public static ItemAmqpDto toItemAmqpDto(Item item) {
        if (item == null) {
            return null;
        }

        return ItemAmqpDto.builder()
                .id(item.getId())
                .content(item.getContent())
                .isCompleted(item.isCompleted())
                .isDeleted(item.isDeleted())
                .start(item.getStart())
                .finish(item.getFinish())
                .completion(item.getCompletion())
                .build();
    }


    public static List<Project> fromProjectAmqpDto(List<ProjectAmqpDto> projectAmqpDtoList) {
        List<Project> projects = new LinkedList<>();
        if (projectAmqpDtoList == null) {
            return projects;
        }

        for (ProjectAmqpDto amqpDtoProject : projectAmqpDtoList) {
            projects.add(fromProjectAmqpDto(amqpDtoProject));
        }
        return projects;
    }

    public static Project fromProjectAmqpDto(ProjectAmqpDto projectAmqpDto) {
        if (projectAmqpDto == null) {
            return null;
        }
        return Project.builder()
                .id(projectAmqpDto.getId())
                .projectName(projectAmqpDto.getName())
                .items(fromItemAmqpDto(projectAmqpDto.getItems()))
                .isDeleted(projectAmqpDto.isDeleted())
                .build();
    }

    public static List<Item> fromItemAmqpDto(List<ItemAmqpDto> itemAmqpDtoList) {
        List<Item> items = new LinkedList<>();
        if (itemAmqpDtoList == null) {
            return items;
        }

        for (ItemAmqpDto amqpDtoItem : itemAmqpDtoList) {
            items.add(fromItemAmqpDto(amqpDtoItem));
        }
        return items;
    }

    private static Item fromItemAmqpDto(ItemAmqpDto amqpDtoItem) {
        if (amqpDtoItem == null) {
            return null;
        }

        return Item.builder()
                .id(amqpDtoItem.getId())
                .isCompleted(amqpDtoItem.isCompleted())
                .start(amqpDtoItem.getStart())
                .finish(amqpDtoItem.getFinish())
                .completion(amqpDtoItem.getCompletion())
                .content(amqpDtoItem.getContent())
                .isDeleted(amqpDtoItem.isDeleted())
                .build();
    }
}
