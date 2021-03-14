package ru.coach2me.csv_parser.utils;

import com.google.gson.Gson;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coach2me.csv_parser.models.Order;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * https://mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
 */

@Component("parser_csv")
public class ParserCsvImpl implements Parser {

    @Autowired
    private Environment environment;

    public void parse(String csvFileName, String jsonFileName) throws IOException {
        String csvFileDir = environment.getProperty("files.dir") + csvFileName;
        String jsonFileDir = environment.getProperty("files.dir") + jsonFileName;

        List<Order> orderList = new CsvToBeanBuilder(new FileReader(csvFileDir))
                .withType(Order.class)
                .build()
                .parse();

        Gson gson = new Gson();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(jsonFileDir));
        bufferedWriter.append('[');

        Iterator<Order> orderIterator = orderList.iterator();
        while (orderIterator.hasNext()) {
            Order order = orderIterator.next();
            try {
                if (!order.getId().isEmpty()) {
                    bufferedWriter.write(gson.toJson(order));
                    System.out.println(gson.toJson(order));
                }
            } catch (IOException e) {
                throw new IllegalArgumentException("Was wrong to writing Json File: " + e);
            }
            if (orderIterator.hasNext() & !order.getId().isEmpty()) {
                bufferedWriter.append(',');
            } else orderIterator.remove();
        }
        bufferedWriter.append(']');
        bufferedWriter.close();
    }
}