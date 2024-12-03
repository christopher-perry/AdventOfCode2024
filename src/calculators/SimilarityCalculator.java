package calculators;

import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import parser.InputParser;
import parser.LeftRightFrequencyMap;

public class SimilarityCalculator {
    private static final Logger LOG = LogManager.getLogger(SimilarityCalculator.class);
    private LeftRightFrequencyMap<Integer> map;
    public SimilarityCalculator(LeftRightFrequencyMap<Integer> map) {
        this.map = map;
    }

    public SimilarityCalculator() {
        this(InputParser.getFrequencyMaps());
    }

    /**
     * Get the similarity score of the input:
     * Calculate a total similarity score by adding up each number in the left list after multiplying it by
     * the number of times that number appears in the right list.
     * @return The Similarity Score
     */
    public int getSimilarity() {
        AtomicInteger similarity = new AtomicInteger();
        map.getLeft().forEach((number, leftFrequency) -> {
            Integer rightFrequency = map.getRightFrequency(number);
            int score = leftFrequency * (number * rightFrequency);
            if (score == 0) {
                LOG.trace("{} left: {}, right: {}, score: {}", number, leftFrequency, rightFrequency, score);
            } else {
                LOG.debug("{} left: {}, right: {}, score: {}", number, leftFrequency, rightFrequency, score);
            }
            similarity.addAndGet(score);
        });
        return similarity.get();
    }
}
