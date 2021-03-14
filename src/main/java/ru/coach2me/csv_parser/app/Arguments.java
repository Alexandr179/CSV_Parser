package ru.coach2me.csv_parser.app;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(separators = "=")
public class Arguments {

	@Parameter(names = {"--csv_file"})
	public String csvFileName;
	
	@Parameter(names = {"--json_file"})
	public String jsonFileName;
}