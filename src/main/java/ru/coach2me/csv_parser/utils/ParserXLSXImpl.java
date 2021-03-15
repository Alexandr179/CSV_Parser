package ru.coach2me.csv_parser.utils;

import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;

/**
 * https://mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
 */

@Component("parser_XLSX")
public class ParserXLSXImpl implements Parser {

    public void parse(String csvFileName, String jsonFileName) {
    }
}