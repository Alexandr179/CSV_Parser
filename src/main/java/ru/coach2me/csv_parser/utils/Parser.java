package ru.coach2me.csv_parser.utils;

import java.io.IOException;

public interface Parser {

    void parse(String csvFileName, String jsonFileName) throws IOException;
}
