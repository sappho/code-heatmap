package uk.org.sappho.code.heatmap.report;

import uk.org.sappho.code.heatmap.engine.simple.Releases;
import uk.org.sappho.warnings.WarningList;

public interface Report {

    public void writeReport(Releases releases, WarningList warnings) throws ReportException;
}
