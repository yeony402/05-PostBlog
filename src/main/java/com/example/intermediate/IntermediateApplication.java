package com.example.intermediate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class IntermediateApplication {

  public static final String APPLICATION_LOCATIONS = "spring.config.location="
          + "classpath:application.yml,"
          + "classpath:aws-s3.yml";

  public static void main(String[] args) {
    SpringApplication.run(IntermediateApplication.class, args);
  }

}
