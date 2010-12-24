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

import uk.org.sappho.code.change.management.data.WarningList;
import uk.org.sappho.code.heatmap.engine.simple.SimpleHeatMapRawDataProcessing;
import uk.org.sappho.configuration.Configuration;

public class FormattedReport implements Report {

    private final Configuration config;
    private static final Logger log = Logger.getLogger(FormattedReport.class);

    @Inject
    public FormattedReport(Configuration config) {

        log.info("Using Formatted Report plugin");
        this.config = config;
    }

    public void writeReport(SimpleHeatMapRawDataProcessing releases, WarningList warnings) throws ReportException {

        Writer writer = null;
        try {
            String title = config.getProperty("formatted.report.title");
            String templateFilename = config.getProperty("formatted.report.template.filename");
            String outputFilename = config.getProperty("formatted.report.output.filename");
            String dateFormat = config.getProperty("formatted.report.date.format", "dd/MM/yyyy HH:mm");
            log.debug("Report parameters:");
            log.debug("title:            " + title);
            log.debug("templateFilename: " + templateFilename);
            log.debug("outputFilename:   " + outputFilename);
            log.debug("dateFormat:       " + dateFormat);
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("title", title);
            data.put("date", new SimpleDateFormat(dateFormat).format(GregorianCalendar.getInstance().getTime()));
            data.put("releases", releases);
            data.put("warnings", warnings);
            freemarker.template.Configuration freemarkerConfig = new freemarker.template.Configuration();
            freemarkerConfig.setClassForTemplateLoading(FormattedReport.class, "/report/templates");
            freemarkerConfig.setObjectWrapper(new DefaultObjectWrapper());
            Template template = freemarkerConfig.getTemplate(templateFilename);
            log.info("Writing formatted report " + outputFilename + " with template " + templateFilename);
            writer = new FileWriter(new File(outputFilename));
            template.process(data, writer);
            writer.close();
            log.debug("Written " + outputFilename);
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
