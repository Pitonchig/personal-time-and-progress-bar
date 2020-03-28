package net.thumbtack.ptpb.db.project;

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
public class Project {
    @Id
    private long id;
    private String userName;
    private String projectName;
    private int color;
    private boolean isFavorited;
}
