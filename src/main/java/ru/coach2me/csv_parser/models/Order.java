package ru.coach2me.csv_parser.models;

import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    @CsvBindByPosition(position = 0)
    private String orderId;

    @CsvBindByPosition(position = 1)
    private String amount;

    @CsvBindByPosition(position = 2)
    private String currency;

    @CsvBindByPosition(position = 3)
    private String comment;

}