package net.thumbtack.ptpb.db;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
@ConfigurationProperties("db")
public class DbProperties {
    private String host;
    private String namespace;
    private int port;
}
