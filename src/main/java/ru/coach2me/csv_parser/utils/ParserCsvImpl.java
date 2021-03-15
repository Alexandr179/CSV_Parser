package ru.coach2me.csv_parser.utils;

import com.google.gson.Gson;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import ru.coach2me.csv_parser.Dto.OrderPojoDto;
import ru.coach2me.csv_parser.app.Arguments;
import ru.coach2me.csv_parser.mapper.OrderPojoMapper;
import ru.coach2me.csv_parser.models.JsonOrder;
import ru.coach2me.csv_parser.models.Order;
import ru.coach2me.csv_parser.services.ThreadsService;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * https://mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
 */

@Component("parser_csv")
//@Scope("prototype")
public class ParserCsvImpl implements Parser {

    @Autowired
    private Environment environment;

    @Autowired
    private OrderPojoMapper orderPojoMapper;

//    @Autowired
//    ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    ThreadsService threadsService;

    public void parse(String csvFileName, String jsonFileName) {
        String csvFileDir = environment.getProperty("files.dir") + csvFileName;
        String jsonFileDir = environment.getProperty("files.dir") + jsonFileName;
        Gson gson = new Gson();
        int lineNumber = 1;

        try (CSVReader reader = new CSVReader(new FileReader(csvFileDir))) {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(jsonFileDir));
            bufferedWriter.append('[');

            Iterator<String[]> orderIterator = reader.iterator();
            while (orderIterator.hasNext()) {
                String[] orderField = orderIterator.next();
                Order order = Order.builder()// TЗ. *Примечание:* все поля обязательны
                        .orderId(orderField[0])
                        .amount(orderField[1])
                        .currency(orderField[2])
                        .comment(orderField[3])
                        .build();

                try {
                    if (!order.getOrderId().isEmpty()) {
                        JsonOrder jsonOrder = JsonOrder.builder()
                                .id(order.getOrderId())
                                .amount(order.getAmount())
                                .currency(order.getCurrency())
                                .comment(order.getComment())
                                .filename(jsonFileName)
                                .line(Integer.toString(lineNumber))
                                .result("OK")
                                .build();

                        try{
                           toOrderPojoDto(order);
                        } catch (Exception e){
                            jsonOrder.setFilename(csvFileName);
                            jsonOrder.setResult("Wrong parsing field(s) order.csv: " + e.getMessage());
                        }

                        try{
                            bufferedWriter.append(gson.toJson(jsonOrder));
                        } catch (Exception e){
                            jsonOrder.setFilename(jsonFileName);
                            jsonOrder.setResult("Wrong writing to order.json: " + e.getMessage());
                        }

                        System.out.println(order);
                        System.out.println("Json Order: " + gson.toJson(jsonOrder));
                        lineNumber += lineNumber;
                    }
                    if (orderIterator.hasNext() & !order.getOrderId().isEmpty()) {
                        bufferedWriter.append(',');
                    }
                } catch (IOException e) {
                    throw new IllegalArgumentException("Was wrong to writing Json File: " + e);
                }
            }

            bufferedWriter.append(']');
            bufferedWriter.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("Was wrong to writing File: " + e);
        }
    }

    private void toOrderPojoDto(Order order) {
        OrderPojoDto orderPojoDto = orderPojoMapper.toDto(order);
    }

}