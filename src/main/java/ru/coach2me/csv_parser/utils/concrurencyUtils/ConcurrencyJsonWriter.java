package ru.coach2me.csv_parser.utils.concrurencyUtils;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import ru.coach2me.csv_parser.Dto.OrderPojoDto;
import ru.coach2me.csv_parser.mapper.OrderPojoMapper;
import ru.coach2me.csv_parser.models.JsonOrder;
import ru.coach2me.csv_parser.models.Order;
import ru.coach2me.csv_parser.utils.ConcurRunnerParserCsvImpl;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import static ru.coach2me.csv_parser.utils.ConcurRunnerParserCsvImpl.jsonFileName;
import static ru.coach2me.csv_parser.utils.ConcurRunnerParserCsvImpl.orderPojoDtoQueue;

/**
 * https://spring.io/guides/gs/async-method/
 */

@Component("concurrencyJsonWriter")
@Scope("prototype")
public class ConcurrencyJsonWriter implements Runnable {

    @Autowired
    private OrderPojoMapper orderPojoMapper;

    @Autowired
    ThreadPoolTaskExecutor taskExecutor;

    private Integer timeCounter;

    @SneakyThrows
    @Override
    public void run() {
//        System.out.println("Thread is: " + Thread.currentThread().getName());
        String jsonFile = ConcurRunnerParserCsvImpl.jsonFile;
        Gson gson = new Gson();
        int lineNumber = 1;

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(jsonFile))) {
            bufferedWriter.append('[');

            while (ConcurRunnerParserCsvImpl.isEmptyPojoDtoQueue()) {
                Thread.currentThread().wait();
            }

            Iterator<OrderPojoDto> orderPojoDtoIterator = ConcurRunnerParserCsvImpl.orderPojoDtoQueue.iterator();

            while (orderPojoDtoIterator.hasNext()) {
                OrderPojoDto orderPojoDto = orderPojoDtoIterator.next();
                ConcurRunnerParserCsvImpl.pollAtOrderPojoDtoQueue();

//              System.out.println("Size of orderPojoDtoQueue (..by ConcurrencyJsonWriter): " + orderPojoDtoQueue.size());
                Order order = orderPojoMapper.toEntity(orderPojoDto);
                JsonOrder jsonOrder = getJsonOrderByOrder(lineNumber, order);

                try {
                    bufferedWriter.append(gson.toJson(jsonOrder));
                } catch (Exception e) {
                    jsonOrder.setResult("Wrong writing to order.json: " + e.getMessage());
                }

                System.out.println(jsonOrder);
                lineNumber += lineNumber;

                if (orderPojoDtoIterator.hasNext()) {
                    bufferedWriter.append(',');
                }

                while (ConcurRunnerParserCsvImpl.isEmptyPojoDtoQueue()){
                    try {
                        wait(50);// pause .. ???????? ?????????????????????? queue..
                    } catch (IllegalMonitorStateException ignored){
                    }
                }
            }
            bufferedWriter.append(']');
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
                .filename(jsonFileName)
                .line(Integer.toString(lineNumber))
                .result("OK")
                .build();
    }
}