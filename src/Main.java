import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import calculators.DistanceCalculator;
import calculators.MasXSearchCalculator;
import calculators.MultSumCalculator;
import calculators.PrintInstructionsCalculator;
import calculators.SafeLevelCalculator;
import calculators.SimilarityCalculator;
import calculators.WordSearchCalculator;

public class Main {
    private static final Logger LOG = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
//        LOG.info("Distance: {}", new DistanceCalculator().getDistance());
//        LOG.info("Similarity Score: {}", new SimilarityCalculator().getSimilarity());
//        LOG.info("Safe Report count: {}", new SafeLevelCalculator().getNumberOfSafeReports());
//        LOG.info("Multiplication sum: {}", new MultSumCalculator().getSumOfProducts());
//        LOG.info("Number of times XMAS appears in puzzle: {}", new WordSearchCalculator().getCount());
//        LOG.info("Number of times MAS appears in an x in puzzle: {}", new MasXSearchCalculator().getCount());
        LOG.info("Count of middle update page numbsers: {}", new PrintInstructionsCalculator().getCount());
    }
}