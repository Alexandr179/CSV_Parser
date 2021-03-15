package ru.coach2me.csv_parser.models;

import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JsonOrder {

    private String id;

    private String amount;

    private String comment;

    private String filename;

    private String currency;

    private String line;

    private String result;
}