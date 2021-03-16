package ru.coach2me.csv_parser;

import com.beust.jcommander.JCommander;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.coach2me.csv_parser.app.Arguments;
import ru.coach2me.csv_parser.config.ApplicationConfig;
import ru.coach2me.csv_parser.utils.ConcurRunnerParserCsvImpl;
import ru.coach2me.csv_parser.utils.Parser;

class Program {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);

        Arguments arguments = new Arguments();
        JCommander.newBuilder()
                .addObject(arguments)
                .build()
                .parse(args);

        String csvFileName = arguments.csvFileName;
        String jsonFileName = arguments.jsonFileName;

// ----------- Common implementation -------------------------------
//        Parser parser = context.getBean(ParserCsvImpl.class);
//        parser.parse(csvFileName, jsonFileName);

// ----------- Concurrency implementation -------------------------------
        Parser parser = context.getBean(ConcurRunnerParserCsvImpl.class);
        parser.parse(csvFileName, jsonFileName);
    }
}