package net.thumbtack.ptpb.handler;

import lombok.extern.slf4j.Slf4j;
import net.thumbtack.ptpb.db.DbConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootApplication
@Import(DbConfiguration.class)
@ComponentScan(basePackages = {
        "net.thumbtack.ptpb"
})
public class PtpbHandler {

    public static void main(String[] args) {
        log.info("Personal time and progress bar handler started.");
        SpringApplication.run(PtpbHandler.class, args);
    }

}