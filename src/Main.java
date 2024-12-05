import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import calculators.DistanceCalculator;
import calculators.SafeLevelCalculator;
import calculators.SimilarityCalculator;

public class Main {
    private static final Logger LOG = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
//        LOG.info("Distance: {}", new DistanceCalculator().getDistance());
//        LOG.info("Similarity Score: {}", new SimilarityCalculator().getSimilarity());
        LOG.info("Safe Report count: {}",
                new SafeLevelCalculator().getNumberOfSafeReports());
    }
}