package net.thumbtack.ptpb.db;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("db")
public class DbProperties {
    private String host;
    private String name;
    private int port;
}
