package parser;

import java.util.Arrays;
import java.util.List;

public class ReportParser {
    private final static String PATH = "resources/reports.txt";

    public static List<Report> parseReport() {
        return InputParser.parseInput(PATH).stream()
                .map(line -> new Report(Arrays.stream(line.split("\\s+")).map(Integer::valueOf).toList()))
                .toList();
    }
}
