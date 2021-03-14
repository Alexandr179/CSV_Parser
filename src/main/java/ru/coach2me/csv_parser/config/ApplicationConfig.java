package ru.coach2me.csv_parser.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "ru.coach2me.csv_parser")
@PropertySource(value = "classpath:application.properties")
public class ApplicationConfig {


}
