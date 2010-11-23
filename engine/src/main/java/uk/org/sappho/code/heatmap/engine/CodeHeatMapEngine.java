package uk.org.sappho.code.heatmap.engine;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import uk.org.sappho.code.heatmap.report.Report;
import uk.org.sappho.code.heatmap.report.ReportException;
import uk.org.sappho.code.heatmap.scm.SCM;
import uk.org.sappho.code.heatmap.scm.SCMException;

public class CodeHeatMapEngine {

    private final SCM scm;
    private final Report report;
    private static final Logger LOG = Logger.getLogger(CodeHeatMapEngine.class);

    @Inject
    public CodeHeatMapEngine(SCM scm, Report report) {

        this.scm = scm;
        this.report = report;
    }

    public void writeReport() throws ReportException, SCMException {

        LOG.debug("Engine starting");
        HeatMapCollection heatMapCollection = new HeatMapCollection();
        scm.processChanges(heatMapCollection);
        report.writeReport(heatMapCollection);
        LOG.debug("Engine finished");
    }
}
