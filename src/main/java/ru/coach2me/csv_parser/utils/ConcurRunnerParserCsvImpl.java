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
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * https://www.youtube.com/watch?v=zxZ0BXlTys0&t=2369s
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
    public static Collection<OrderPojoDto> orderPojoDtoList = new CopyOnWriteArrayList<>();


    public void parse(String csvFileName, String jsonFileName) throws InterruptedException {
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

        ConcurrencyCsvReader reader = (ConcurrencyCsvReader) context.getBean("concurrencyCsvReader");
        ConcurrencyJsonWriter writer = (ConcurrencyJsonWriter) context.getBean("concurrencyJsonWriter");

        if (concurrencyCsvReader.csvReader != null){
            taskExecutor.newThread(reader).start();
            Thread.sleep(200);// 100 ..мало
            taskExecutor.newThread(writer).start();
        }
    }
}