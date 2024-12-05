package calculators;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import parser.Report;
import parser.ReportParser;

public class SafeLevelCalculator {
    private final static Logger LOG = LogManager.getLogger(SafeLevelCalculator.class);
    final List<Report> reports;

    public SafeLevelCalculator(List<Report> reports) {
        this.reports = reports;
    }

    public SafeLevelCalculator() {
        this(ReportParser.parseReport());
    }

    public SafeLevelCalculator(String path) {
        this(ReportParser.parseReport(path));
    }

    public long getNumberOfSafeReports() {
        return reports.stream()
                .filter(Report::isSafe)
                .count();
    }
}
