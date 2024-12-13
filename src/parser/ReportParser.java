package parser;

import java.util.Arrays;
import java.util.List;

public class ReportParser {
    private final static String PATH = "resources/input/reports.txt";

    public static List<Report> parseReport() {
        return parseReport(PATH);
    }

    public static List<Report> parseReport(String path) {
        return InputParser.parseInput(path).stream()
                .map(line -> new Report(Arrays.stream(line.split("\\s+")).map(Integer::valueOf).toList()))
                .toList();
    }
}
