package uk.org.sappho.code.heatmap.engine;

import org.apache.log4j.Logger;

import uk.org.sappho.code.heatmap.scm.SCM;

public class CodeHeatMapEngine {

    private final SCM scm;
    private static final Logger LOG = Logger.getLogger(CodeHeatMapEngine.class);

    public CodeHeatMapEngine(SCM scm) {

        this.scm = scm;
    }

    public HeatMapCollection processChanges() {

        HeatMapCollection hmc = new HeatMapCollection();
        scm.processChanges(hmc);
        return hmc;
    }
}
