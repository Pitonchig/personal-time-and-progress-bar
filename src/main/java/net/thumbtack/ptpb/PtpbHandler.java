package net.thumbtack.ptpb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class PtpbHandler {
    public static void main(String[] args) {
        log.info("Personal time and progress bar handler started.");
        SpringApplication.run(PtpbHandler.class, args);
    }
}
