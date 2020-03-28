package net.thumbtack.ptpb.db.user;

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
public class User implements Serializable {
    @Id
    private String name;
    private String password;
    private String token;
    private LocalDateTime registered;
}


