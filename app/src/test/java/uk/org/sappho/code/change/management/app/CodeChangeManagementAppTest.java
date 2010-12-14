package uk.org.sappho.code.change.management.app;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Guice;
import com.google.inject.Injector;

import uk.org.sappho.code.change.management.data.persistence.ConfigurationRawDataPersistence;
import uk.org.sappho.code.change.management.data.persistence.FilenameRawDataPersistence;
import uk.org.sappho.code.change.management.data.persistence.ReaderRawDataPersistence;
import uk.org.sappho.configuration.Configuration;
import uk.org.sappho.configuration.ConfigurationException;

@RunWith(MockitoJUnitRunner.class)
public class CodeChangeManagementAppTest {

    @Mock
    private Configuration mockConfiguration;

    @Before
    public void setupFakeWiringConfiguration() throws ConfigurationException {
        returnFakePluginFor("scm.plugin", "uk.org.sappho.code.change.management.scm");
        returnFakePluginFor("report.plugin", "uk.org.sappho.code.heatmap.report");
        returnFakePluginFor("issues.plugin", "uk.org.sappho.code.change.management.issues");
        returnFakePluginFor("engine.plugin", "uk.org.sappho.code.heatmap.engine");
    }

    @Test
    public void shouldWireExplicitly() {
        Guice.createInjector(new CodeChangeManagementApp(mockConfiguration));
    }

    @Test
    // TODO: better to use annotations to distinguish specialisations,
    // if we really need specialisations like this - probably not
    public void shouldWireRawDataPersistenceImplementations() {
        Injector injector = Guice.createInjector(new CodeChangeManagementApp(mockConfiguration));
        injector.getInstance(ReaderRawDataPersistence.class);
        injector.getInstance(FilenameRawDataPersistence.class);
        injector.getInstance(ConfigurationRawDataPersistence.class);
    }

    private void returnFakePluginFor(String name, String defaultPackage) throws ConfigurationException {
        Class<FakePlugin> getSCMPluginClass = mockConfiguration.getPlugin(name,
                defaultPackage);
        when(getSCMPluginClass).thenReturn(FakePlugin.class);
    }
}
