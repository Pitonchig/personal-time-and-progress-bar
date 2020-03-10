package net.thumbtack.ptpb.db.task;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Task {
    @Id
    private Integer id;
    private String description;
}
