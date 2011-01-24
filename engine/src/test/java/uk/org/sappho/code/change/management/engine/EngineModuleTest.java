package uk.org.sappho.code.change.management.engine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.org.sappho.code.change.management.scm.SCMException;
import uk.org.sappho.configuration.Configuration;
import uk.org.sappho.configuration.ConfigurationException;

@RunWith(MockitoJUnitRunner.class)
public class EngineModuleTest {

    @Mock
    private Configuration mockConfiguration;

    @Before
    public void setupFakeWiringConfiguration() throws ConfigurationException {

        /**
        Class<FakeSCMPlugin> getSCMPluginClass = mockConfiguration.getPlugin("scm.plugin",
                "uk.org.sappho.code.change.management.scm");
        when(getSCMPluginClass).thenReturn(FakeSCMPlugin.class);
        Class<FakeIssueManagementPlugin> getIssueManagementPluginClass = mockConfiguration.getPlugin("issues.plugin",
                "uk.org.sappho.code.change.management.issues");
        when(getIssueManagementPluginClass).thenReturn(FakeIssueManagementPlugin.class);
        Class<FakeRawDataProcessingPlugin> getRawDataProcessingPluginClass = mockConfiguration.getPlugin(
                "raw.data.processing.plugin", "uk.org.sappho.code.change.management.processor");
        when(getRawDataProcessingPluginClass).thenReturn(FakeRawDataProcessingPlugin.class);
        module = new StandardEngineModule(mockConfiguration);
        injector = Guice.createInjector(module);
        module.setInjector(injector);
        **/
    }

    @Test
    public void shouldWireInPlugins() throws SCMException, RawDataProcessingException, ConfigurationException {

        /**
        module.getSCMPlugin().scan(new RawData());
        module.getIssueManagementPlugin().getIssueData("");
        module.getRawDataProcessingPlugin().run(new RawData());
        **/
    }
}
