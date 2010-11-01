package uk.org.sappho.code.heatmap.engine;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import uk.org.sappho.code.heatmap.report.Report;
import uk.org.sappho.code.heatmap.scm.SCM;

public class CodeHeatMapEngine implements Engine {

    private final SCM scm;
    private final Report report;
    private static final Logger LOG = Logger.getLogger(CodeHeatMapEngine.class);

    @Inject
    public CodeHeatMapEngine(SCM scm, Report report) {

        this.scm = scm;
        this.report = report;
    }

    public void writeReport() {

        LOG.debug("Engine starting");
        report.writeReport(scm.processChanges());
        LOG.debug("Engine finished");
    }
}
