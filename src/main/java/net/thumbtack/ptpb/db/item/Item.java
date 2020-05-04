package net.thumbtack.ptpb.db.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;


@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    private long id;
    private long projectId;
    private long userId;

    private String content;
    private boolean isCompleted;
}
