package uk.org.sappho.code.heatmap.report;

import uk.org.sappho.code.heatmap.engine.simple.Releases;

public interface Report {

    public void writeReport(Releases releases) throws ReportException;
}
