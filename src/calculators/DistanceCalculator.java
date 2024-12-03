package calculators;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import parser.InputParser;
import parser.LeftRightList;

public class DistanceCalculator {
    private static final Logger LOG = LogManager.getLogger(DistanceCalculator.class);
    private final List<Integer> left;
    private final List<Integer> right;

    public DistanceCalculator(LeftRightList<Integer> lists) {
        left = lists.getLeft();
        right = lists.getRight();
    }

    public DistanceCalculator() {
        this(InputParser.getSortedLists());
    }

    public int getDistance() {
        int distance = 0;
        for (int i = 0; i < left.size(); i++) {
            int l = left.get(i);
            int r = right.get(i);
            int difference = Math.abs(l - r);
            LOG.trace("{} {}: {}", l, r, distance);
            distance += difference;
        }
        return distance;
    }
}
