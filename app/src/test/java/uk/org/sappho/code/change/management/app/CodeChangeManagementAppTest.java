package uk.org.sappho.code.change.management.app;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Module;

import uk.org.sappho.configuration.Configuration;
import uk.org.sappho.configuration.ConfigurationException;

@RunWith(MockitoJUnitRunner.class)
public class CodeChangeManagementAppTest {

    @Mock
    private Configuration config;

    private List<String> actions;
    private List<String> returnedActions;

    @Before
    public void setupFakeWiringConfiguration() throws ConfigurationException {

        String engineModuleName = "modules.engine";
        List<String> returnedModuleNames = new ArrayList<String>();
        returnedModuleNames.add(engineModuleName);

        List<String> moduleNames = config.getPropertyList("module");
        when(moduleNames).thenReturn(returnedModuleNames);

        Module engineModule = config.getGuiceModule(engineModuleName);
        when(engineModule).thenReturn(new FakeEngineModule());

        returnedActions = new ArrayList<String>();
        actions = config.getPropertyList("app.run.action");
    }

    @Test
    public void shouldRun() throws Throwable {

        returnedActions.add("scan");
        returnedActions.add("validate");
        returnedActions.add("save");
        returnedActions.add("load");
        returnedActions.add("refresh");
        returnedActions.add("process");
        when(actions).thenReturn(returnedActions);
        new CodeChangeManagementApp().run(config);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldFail() throws Throwable {

        returnedActions.add("snafu");
        when(actions).thenReturn(returnedActions);
        new CodeChangeManagementApp().run(config);
    }
}
