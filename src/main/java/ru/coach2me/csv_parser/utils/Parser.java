package ru.coach2me.csv_parser.utils;

public interface Parser {

    void parse(String csvFileName, String jsonFileName) throws InterruptedException;
}
