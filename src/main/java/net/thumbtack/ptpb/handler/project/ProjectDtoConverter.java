package net.thumbtack.ptpb.handler.project;

import net.thumbtack.ptpb.db.user.Item;
import net.thumbtack.ptpb.db.user.Project;
import net.thumbtack.ptpb.handler.project.dto.ItemDto;
import net.thumbtack.ptpb.handler.project.dto.response.ProjectResponse;
import net.thumbtack.ptpb.handler.project.dto.request.UpdateProjectRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class ProjectDtoConverter {

    static public ItemDto toItemDto(Item item) {
        if (item == null) {
            return null;
        }
//        ZonedDateTime start = localDateTimeToZonedDateTime(item.getStart());
//        ZonedDateTime finish = localDateTimeToZonedDateTime(item.getFinish());
//        ZonedDateTime completion = localDateTimeToZonedDateTime(item.getCompletion());

        return ItemDto.builder()
                .id(item.getId())
                .isCompleted(item.isCompleted())
                .content(item.getContent())
                .start(item.getStart())
                .finish(item.getFinish())
                .isDeleted(item.isDeleted())
                .completion(item.getCompletion())
                .build();
    }

    static public List<ItemDto> toItemDtoList(List<Item> items) {
        List<ItemDto> responses = new LinkedList<>();
        for (Item item : items) {
            responses.add(toItemDto(item));
        }
        return responses;
    }

    static public ProjectResponse toProjectResponse(Project project) {
        List<Item> items = project.getItems();
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getProjectName())
                .items(toItemDtoList(items))
                .isDeleted(project.isDeleted())
                .build();
    }

    static public List<ProjectResponse> toProjectResponseList(List<Project> projects) {
        List<ProjectResponse> responses = new LinkedList<>();

        if (projects == null) {
            return responses;
        }

        for (Project project : projects) {
            responses.add(toProjectResponse(project));
        }
        return responses;
    }

    static public List<Project> fromUpdateProjectRequestList(List<UpdateProjectRequest> request) {
        List<Project> projects = new LinkedList<>();

        for (UpdateProjectRequest projectRequest : request) {
            List<Item> items = new LinkedList<>();
            projectRequest.getItems().forEach(i -> {
                String itemUuid = (i.getId() == null || i.getId().isEmpty()) ? UUID.randomUUID().toString() : i.getId();
//                LocalDateTime start = zonedDateTimeToLocalDateTime(i.getStart());
//                LocalDateTime finish = zonedDateTimeToLocalDateTime(i.getFinish());
//                LocalDateTime completion = zonedDateTimeToLocalDateTime(i.getCompletion());

                Item item = Item.builder()
                        .id(itemUuid)
                        .content(i.getContent())
                        .start(i.getStart())
                        .finish(i.getFinish())
                        .completion(i.getCompletion())
                        .isCompleted(i.isCompleted())
                        .isDeleted(i.isDeleted())
                        .build();
                items.add(item);
            });

            String projectUuid = (projectRequest.getId() == null || projectRequest.getId().isEmpty()) ? UUID.randomUUID().toString() : projectRequest.getId();
            Project project = Project.builder()
                    .id(projectUuid)
                    .projectName(projectRequest.getName())
                    .items(items)
                    .isDeleted(projectRequest.isDeleted())
                    .build();
            projects.add(project);
        }
        return projects;
    }

    static private ZonedDateTime localDateTimeToZonedDateTime(LocalDateTime localDateTime) {
        return (localDateTime == null) ? null : ZonedDateTime.of(localDateTime.toLocalDate(), localDateTime.toLocalTime(), ZoneId.of("UTC"));
    }

    static private LocalDateTime zonedDateTimeToLocalDateTime(ZonedDateTime zonedDateTime) {
        return (zonedDateTime == null) ? null : zonedDateTime.toLocalDateTime();
    }
}
