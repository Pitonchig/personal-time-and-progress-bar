package net.thumbtack.ptpb.db.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.time.LocalDateTime;


@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private String id;
    private String content;
    private boolean isCompleted;
    private LocalDateTime start;
    private LocalDateTime finish;
}
