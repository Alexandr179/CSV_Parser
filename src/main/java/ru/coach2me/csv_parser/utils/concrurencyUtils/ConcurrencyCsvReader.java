package ru.coach2me.csv_parser.utils.concrurencyUtils;

import com.opencsv.CSVReader;
import org.modelmapper.MappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import ru.coach2me.csv_parser.Dto.OrderPojoDto;
import ru.coach2me.csv_parser.mapper.OrderPojoMapper;
import ru.coach2me.csv_parser.models.JsonOrder;
import ru.coach2me.csv_parser.models.Order;
import ru.coach2me.csv_parser.utils.ConcurRunnerParserCsvImpl;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import static ru.coach2me.csv_parser.utils.ConcurRunnerParserCsvImpl.csvFileName;

/**
 * https://spring.io/guides/gs/async-method/
 */

@Component("concurrencyCsvReader")
@Scope("prototype")
public class ConcurrencyCsvReader implements Runnable {

    @Autowired
    private OrderPojoMapper orderPojoMapper;

    @Autowired
    ThreadPoolTaskExecutor taskExecutor;

    public CSVReader csvReader;

    /**
     * in Concurrency возможно ..обращение к  полям классов, сторонним для потока только напрямую **
     * но НЕ через   @Autowired
     * ConcurRunnerParserCsvImpl concurrencyParserCsv;
     */
    @Override
    public void run() {// вручную выст. synchronized....
//        System.out.println("Thread is readerThread: " + Thread.currentThread().getName());
        String csvFile = ConcurRunnerParserCsvImpl.csvFile;

        int lineNumber = 1;
        try (CSVReader csvReader = new CSVReader(new FileReader(csvFile))) {
            Iterator<String[]> orderIterator = csvReader.iterator();
            while (orderIterator.hasNext()) {
                String[] orderField = orderIterator.next();
                Order order = getOrderByStrings(orderField);

                if (!order.getOrderId().isEmpty()) {
                    JsonOrder outOrder = getJsonOrderByOrder(lineNumber, order);
                    try {
                        OrderPojoDto orderPojoDto = orderPojoMapper.toDto(order);
                        ConcurRunnerParserCsvImpl.addToOrderPojoDtoQueue(orderPojoDto);
                        notify();

                    } catch (MappingException e) {
                        outOrder.setFilename("csvFile");
                        outOrder.setResult("Wrong parsing field(s) order.csv: " + e.getMessage());
                    } catch (Exception e) {
//                        outOrder.setResult("Wrong Concurrency with parsing field(s) order.csv: " + e.getMessage());
                    }

                    System.out.println(outOrder);
                    lineNumber += lineNumber;
                }
            }
            Thread.currentThread().stop();
        } catch (IOException e) {
            throw new IllegalArgumentException("Was wrong to writing File: " + e);
        }
    }


    private JsonOrder getJsonOrderByOrder(int lineNumber, Order order) {
        return JsonOrder.builder()
                .id(order.getOrderId())
                .amount(order.getAmount())
                .currency(order.getCurrency())
                .comment(order.getComment())
                .filename(csvFileName)
                .line(Integer.toString(lineNumber))
                .result("OK")
                .build();
    }

    Order getOrderByStrings(String[] orderField) {
        return Order.builder()// (TЗ) *Примечание:* все поля обязательны
                .orderId(orderField[0])
                .amount(orderField[1])
                .currency(orderField[2])
                .comment(orderField[3])
                .build();
    }
}