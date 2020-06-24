package net.thumbtack.ptpb.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class HandlerConfiguration implements WebMvcConfigurer {

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*", "http://127.0.0.1:8080");
//                //.allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "OPTIONS");
//    }

    @Bean
    public WebMvcConfigurer corsConfigurer()
    {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://127.0.0.1:8090", "http://localhost:8080", "*")
                        .allowedMethods("GET", "POST","PUT", "DELETE", "HEAD", "OPTIONS", "PATCH")
                        .allowCredentials(true);
            }
        };
    }

}
