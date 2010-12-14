package uk.org.sappho.code.heatmap.report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

import uk.org.sappho.code.heatmap.engine.simple.Releases;
import uk.org.sappho.configuration.Configuration;
import uk.org.sappho.warnings.WarningList;

public class FormattedReport implements Report {

    private final WarningList warnings;
    private final Configuration config;
    private static final Logger LOG = Logger.getLogger(FormattedReport.class);

    @Inject
    public FormattedReport(WarningList warnings, Configuration config) {

        LOG.info("Using Formatted Report plugin");
        this.warnings = warnings;
        this.config = config;
    }

    public void writeReport(Releases releases) throws ReportException {

        Writer writer = null;
        try {
            String title = config.getProperty("formatted.report.title");
            String templateFilename = config.getProperty("formatted.report.template.filename");
            String outputFilename = config.getProperty("formatted.report.output.filename");
            String dateFormat = config.getProperty("formatted.report.date.format", "dd/MM/yyyy HH:mm");
            LOG.debug("Report parameters:");
            LOG.debug("title:            " + title);
            LOG.debug("templateFilename: " + templateFilename);
            LOG.debug("outputFilename:   " + outputFilename);
            LOG.debug("dateFormat:       " + dateFormat);
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("title", title);
            data.put("date", new SimpleDateFormat(dateFormat).format(GregorianCalendar.getInstance().getTime()));
            data.put("releases", releases);
            data.put("warnings", warnings);
            freemarker.template.Configuration freemarkerConfig = new freemarker.template.Configuration();
            freemarkerConfig.setClassForTemplateLoading(FormattedReport.class, "/report/templates");
            freemarkerConfig.setObjectWrapper(new DefaultObjectWrapper());
            Template template = freemarkerConfig.getTemplate(templateFilename);
            LOG.info("Writing formatted report " + outputFilename);
            writer = new FileWriter(new File(outputFilename));
            template.process(data, writer);
            writer.close();
            LOG.debug("Written " + outputFilename);
        } catch (Throwable t) {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
            throw new ReportException("Unable to write report", t);
        }
    }
}
