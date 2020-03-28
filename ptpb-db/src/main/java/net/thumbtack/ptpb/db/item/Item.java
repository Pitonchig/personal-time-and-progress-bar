package net.thumbtack.ptpb.db.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    private long id;
    private long projectId;
    private String userName;
    private String content;
    private int priority;
    private LocalDateTime due;
    private boolean isCompleted;
}
