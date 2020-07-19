package net.thumbtack.ptpb.db.session;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    @Id
    private String uuid;
    private String userId;
    private ZonedDateTime dateTime;
    private boolean isExpired;
}
