package net.thumbtack.ptpb.db.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize
public class User {
    @Id
    private String id;
    private String name;
    private String password;
    private String email;
    private LocalDateTime registration;
}


