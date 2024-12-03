package parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InputParser {
    private static final String PATH = "resources/input.txt";
    private static final Logger LOG = LogManager.getLogger(InputParser.class);

    public static List<String> parseInput(String path) {
        List<String> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line);
                LOG.trace(line);
            }
        } catch (IOException ioe) {
            LOG.error("Exception in reading file: ", ioe);
        }
        return result;
    }

    public static List<String> parseInput() {
        return parseInput(PATH);
    }

    public static LeftRightList<Integer> getSortedLists() {
        List<String> result = parseInput();
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        result.forEach(line -> {
            String[] pair = line.split("\\s+");
            left.add(Integer.valueOf(pair[0]));
            right.add(Integer.valueOf(pair[1]));
        });
        return new LeftRightList<>(left.stream().sorted().toList(), right.stream().sorted().toList());
    }

    public static LeftRightFrequencyMap<Integer> getFrequencyMaps() {
        List<String> result = parseInput();
        Map<Integer, Integer> left = new HashMap<>();
        Map<Integer, Integer> right = new HashMap<>();
        result.forEach(line -> {
            try {
                String [] pair = line.split("\\s+");
                Integer l = Integer.valueOf(pair[0]);
                left.compute(l, incrementElseOne);
                Integer r = Integer.valueOf(pair[1]);
                right.compute(r, incrementElseOne);
            } catch (NumberFormatException e) {
                LOG.error("Line could not be parsed into integers: {}", line, e);
            }
        });
        return new LeftRightFrequencyMap<>(left, right);
    }

    private static BiFunction<Integer, Integer, Integer> incrementElseOne = (k, v) -> (v == null) ? 1 : v + 1;
}
