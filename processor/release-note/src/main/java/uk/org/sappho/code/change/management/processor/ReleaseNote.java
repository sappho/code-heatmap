package uk.org.sappho.code.change.management.processor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.code.change.management.data.RevisionData;
import uk.org.sappho.code.change.management.engine.RawDataProcessing;
import uk.org.sappho.code.change.management.engine.RawDataProcessingException;
import uk.org.sappho.code.heatmap.mapping.HeatMapSelector;
import uk.org.sappho.configuration.Configuration;
import uk.org.sappho.configuration.ConfigurationException;

public class ReleaseNote implements RawDataProcessing {

    private final Configuration config;
    private static final Logger log = Logger.getLogger(ReleaseNote.class);

    @Inject
    public ReleaseNote(Configuration config) throws ConfigurationException {

        log.info("Using release note plugin");
        this.config = config;
    }

    public void run(RawData rawData) throws RawDataProcessingException, ConfigurationException {

        HeatMapSelector heatMapSelector = (HeatMapSelector) config
                .getGroovyScriptObject("release.note.mapper.heatmap.selector");
        HeatMaps heatMaps = new HeatMaps();
        String release = null;
        for (String revisionKey : rawData.getRevisionKeys()) {
            RevisionData revisionData = rawData.getRevisionData(revisionKey);
            String issueKey = revisionData.getIssueKey();
            IssueData issueData = rawData.getIssueData(issueKey);
            if (issueData == null)
                throw new RawDataProcessingException("Unable to get issue data for revision " + revisionKey);
            List<String> issueReleases = issueData.getReleases();
            if (issueReleases.size() != 1)
                throw new RawDataProcessingException("There is not exactly one release for issue " + issueKey);
            String issueRelease = issueReleases.get(0);
            if (release == null)
                release = issueRelease;
            else if (!release.equals(issueRelease))
                throw new RawDataProcessingException("Release is different to previous release for issue " + issueKey);
            heatMaps.add(revisionData, issueData, heatMapSelector);
        }
        Writer writer = null;
        try {
            File templateFile = new File(config.getProperty("release.note.template.filename")).getCanonicalFile();
            File outputFile = new File(config.getProperty("release.note.output.filename")).getCanonicalFile();
            String dateFormat = config.getProperty("release.note.date.format", "dd/MM/yyyy HH:mm");
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("config", config);
            data.put("release", release);
            data.put("date", new SimpleDateFormat(dateFormat).format(GregorianCalendar.getInstance().getTime()));
            data.put("heatMaps", heatMaps);
            data.put("warnings", rawData.getWarnings());
            freemarker.template.Configuration freemarkerConfig = new freemarker.template.Configuration();
            freemarkerConfig.setObjectWrapper(new DefaultObjectWrapper());
            freemarkerConfig.setDirectoryForTemplateLoading(new File(templateFile.getParent()));
            Template template = freemarkerConfig.getTemplate(templateFile.getName());
            log.info("Writing release note " + outputFile.getPath() + " with template " + templateFile.getPath());
            writer = new FileWriter(outputFile);
            template.process(data, writer);
            writer.close();
            log.info("Completed release note");
        } catch (Throwable t) {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
            throw new RawDataProcessingException("Unable to write release note", t);
        }
    }
}
