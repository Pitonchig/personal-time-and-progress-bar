package net.thumbtack.ptpb.db.session;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Session implements Serializable {
    @Id
    private String uuid;
    private String userId;
    private LocalDateTime dateTime;
    private boolean isExpired;
}
