package net.thumbtack.ptpb.db.user;


import lombok.*;

import javax.persistence.Entity;
import java.util.List;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    private String id;
    private String projectName;
    @Singular
    private List<Item> items;
}
