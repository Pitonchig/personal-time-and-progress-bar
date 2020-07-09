package net.thumbtack.ptpb;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.text.DateFormat;
import java.time.ZoneId;
import java.util.TimeZone;

@Slf4j
@SpringBootApplication
public class PtpbHandler {
    public static void main(String[] args) {
        log.info("Personal time and progress bar handler started.");
        SpringApplication.run(PtpbHandler.class, args);
    }

//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        mapper.setTimeZone(TimeZone.getTimeZone("UTC"));
//        return mapper;
//    }
}
