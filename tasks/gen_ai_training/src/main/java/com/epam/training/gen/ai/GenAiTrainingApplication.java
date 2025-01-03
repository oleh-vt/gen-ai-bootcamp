package com.epam.training.gen.ai;

import com.epam.training.gen.ai.config.QdrantConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:/config/application.properties")
@EnableConfigurationProperties(QdrantConfigProperties.class)
public class GenAiTrainingApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenAiTrainingApplication.class, args);
    }

}
