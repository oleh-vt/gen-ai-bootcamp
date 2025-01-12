package com.epam.training.gen.ai;

import com.epam.training.gen.ai.config.QdrantConnectionProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:/config/application.properties")
@EnableConfigurationProperties(QdrantConnectionProperties.class)
public class GenAiRagApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenAiRagApplication.class, args);
    }

}
