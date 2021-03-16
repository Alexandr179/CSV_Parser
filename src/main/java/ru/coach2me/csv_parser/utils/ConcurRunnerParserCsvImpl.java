package ru.coach2me.csv_parser.utils;

import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import ru.coach2me.csv_parser.Dto.OrderPojoDto;
import ru.coach2me.csv_parser.config.ApplicationConfig;
import ru.coach2me.csv_parser.utils.concrurencyUtils.ConcurrencyCsvReader;
import ru.coach2me.csv_parser.utils.concrurencyUtils.ConcurrencyJsonWriter;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * https://mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
 */

@Component("concurrencyParserCsvImpl")
public class ConcurRunnerParserCsvImpl implements Parser {

    @Autowired
    private Environment environment;

    @Autowired
    ConcurrencyCsvReader concurrencyCsvReader;

    public static String filesDir;
    public static String csvFile;
    public static String jsonFile;
    public static String csvFileName;
    public static String jsonFileName;
    public static List<OrderPojoDto> orderPojoDtoList = new CopyOnWriteArrayList<>();


    public void parse(String csvFileName, String jsonFileName) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) context.getBean("taskExecutor");
        filesDir = environment.getProperty("files.dir");
        csvFile = environment.getProperty("files.dir") + csvFileName;
        jsonFile = environment.getProperty("files.dir") + jsonFileName;
        ConcurRunnerParserCsvImpl.csvFileName = csvFileName;
        ConcurRunnerParserCsvImpl.jsonFileName = jsonFileName;


        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            concurrencyCsvReader.csvReader = reader;
        } catch (IOException e) {
            throw new IllegalArgumentException("File not found: " + e);
        }


        ConcurrencyCsvReader parser = (ConcurrencyCsvReader) context.getBean("concurrencyCsvReader");
        ConcurrencyJsonWriter writer = (ConcurrencyJsonWriter) context.getBean("concurrencyJsonWriter");

        if (concurrencyCsvReader.csvReader != null){
//            taskExecutor.execute(parser);
            taskExecutor.newThread(parser).start();
            taskExecutor.newThread(writer).start();
        }
        taskExecutor.newThread(parser).stop();
        taskExecutor.newThread(writer).stop();
    }
}