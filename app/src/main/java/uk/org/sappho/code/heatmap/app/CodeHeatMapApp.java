package uk.org.sappho.code.heatmap.app;

import org.apache.log4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import uk.org.sappho.code.heatmap.config.Configuration;
import uk.org.sappho.code.heatmap.config.impl.ConfigurationFile;
import uk.org.sappho.code.heatmap.engine.CodeHeatMapEngine;
import uk.org.sappho.code.heatmap.issues.IssueManagement;
import uk.org.sappho.code.heatmap.report.Report;
import uk.org.sappho.code.heatmap.scm.SCM;

public class CodeHeatMapApp extends AbstractModule {

    private static final Logger LOG = Logger.getLogger(CodeHeatMapApp.class);

    @SuppressWarnings("unchecked")
    @Override
    protected void configure() {

        try {
            LOG.debug("Configuring plugins");
            bind(Configuration.class).to(ConfigurationFile.class);
            bind(SCM.class).to((Class<? extends SCM>) Class.forName(System.getProperty("scm.implementation")));
            bind(Report.class).to((Class<? extends Report>) Class.forName(System.getProperty("report.implementation")));
            bind(IssueManagement.class).to(
                    (Class<? extends IssueManagement>) Class.forName(System.getProperty("issues.implementation")));
        } catch (Throwable t) {
            LOG.error("Unable to bind plugin classes", t);
        }
    }

    public static void main(String[] args) {

        try {
            Injector injector = Guice.createInjector(new CodeHeatMapApp());
            CodeHeatMapEngine engine = injector.getInstance(CodeHeatMapEngine.class);
            engine.writeReport();
        } catch (Throwable t) {
            LOG.error("Application error", t);
        }
    }
}
