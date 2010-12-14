package uk.org.sappho.code.change.management.app;

import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Guice;

import uk.org.sappho.code.heatmap.mapping.HeatMapSelector;
import uk.org.sappho.configuration.Configuration;
import uk.org.sappho.configuration.ConfigurationException;
import uk.org.sappho.string.mapping.Mapper;

@RunWith(MockitoJUnitRunner.class)
public class CodeChangeManagementAppTest {

    @Mock
    private Configuration mockConfiguration;
    @Mock
    private HeatMapSelector mockHeatMapSelector;
    @Mock
    private Mapper mockMapper;

    @Test
    public void shouldWire() throws ConfigurationException {
        when(mockConfiguration.getGroovyScriptObject("mapper.heatmap.selector"))
                .thenReturn(mockHeatMapSelector);
        when(mockConfiguration.getGroovyScriptObject("mapper.commit.comment.to.issue.key"))
                .thenReturn(mockMapper);
        returnFakePluginFor("scm.plugin", "uk.org.sappho.code.change.management.scm");
        returnFakePluginFor("report.plugin", "uk.org.sappho.code.heatmap.report");
        returnFakePluginFor("issues.plugin", "uk.org.sappho.code.change.management.issues");
        returnFakePluginFor("engine.plugin", "uk.org.sappho.code.heatmap.engine");

        Guice.createInjector(new CodeChangeManagementApp(mockConfiguration));
    }

    private void returnFakePluginFor(String name, String defaultPackage) throws ConfigurationException {
        Class<FakePlugin> getSCMPluginClass = mockConfiguration.getPlugin(name,
                defaultPackage);
        when(getSCMPluginClass).thenReturn(FakePlugin.class);
    }
}
