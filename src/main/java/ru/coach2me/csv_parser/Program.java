package ru.coach2me.csv_parser;

import com.beust.jcommander.JCommander;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import ru.coach2me.csv_parser.app.Arguments;
import ru.coach2me.csv_parser.config.ApplicationConfig;
import ru.coach2me.csv_parser.utils.Parser;
import ru.coach2me.csv_parser.utils.ParserCsvImpl;

import java.io.IOException;


class Program {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
//        ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) context.getBean("taskExecutor");

        Arguments arguments = new Arguments();
        JCommander.newBuilder()
                .addObject(arguments)
                .build()
                .parse(args);

        String csvFileName = arguments.csvFileName;
        String jsonFileName = arguments.jsonFileName;

        Parser parser = context.getBean(ParserCsvImpl.class);
        parser.parse(csvFileName, jsonFileName);
    }
}