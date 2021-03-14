package ru.coach2me.csv_parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.coach2me.csv_parser.app.Arguments;
import ru.coach2me.csv_parser.config.ApplicationConfig;
import ru.coach2me.csv_parser.utils.Parser;
import ru.coach2me.csv_parser.utils.ParserCsvImpl;
import com.beust.jcommander.JCommander;
import java.io.IOException;

class Program {

	@Autowired
	@Qualifier(value = "parser_csv")
	Parser parser;

	public static void main(String[] args) throws IOException {
		ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);

		Arguments arguments = new Arguments();
		JCommander.newBuilder()
			.addObject(arguments)
			.build()
			.parse(args);

		String csvFileName = arguments.csvFileName;
		String jsonFileName = arguments.jsonFileName;

		Parser parser = context.getBean(ParserCsvImpl.class);
		parser.parse(csvFileName, jsonFileName);
	}
}