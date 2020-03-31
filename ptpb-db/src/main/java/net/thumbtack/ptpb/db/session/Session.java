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
@AllArgsConstructor
@NoArgsConstructor
public class Session implements Serializable {
    @Id
    private String uuid;
    private LocalDateTime dateTime;
    private long userId;
    private boolean isExpired;
}
