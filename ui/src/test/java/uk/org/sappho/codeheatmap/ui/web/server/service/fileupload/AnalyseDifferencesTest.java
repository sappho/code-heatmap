package uk.org.sappho.codeheatmap.ui.web.server.service.fileupload;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.org.sappho.codeheatmap.ui.web.server.database.Database;
import uk.org.sappho.codeheatmap.ui.web.server.service.fileupload.AnalyseDifferences;
import uk.org.sappho.codeheatmap.ui.web.shared.model.Party;

@RunWith(MockitoJUnitRunner.class)
public class AnalyseDifferencesTest {

    private final Party FRED = createParty("1", "Fred");
    private final Party FREDRICA = createParty("1", "Fredrica");
    private final Party GEORGE = createParty("2", "George");
    private final Party NOMAN = createParty("1", null);

    @Mock
    private Database mockDatabase;
    private AnalyseDifferences analyseDifferences;

    @Before
    public void setupSut() {
        Set<String> headers = new HashSet<String>(asList("name"));
        when(mockDatabase.getInternalHeaderNames()).thenReturn(headers);
        analyseDifferences = new AnalyseDifferences(mockDatabase);
    }

    @Test
    public void shouldFindNoDifferencesForIdenticalParty() {
        internalPartiesAre(FRED);
        lastImportedPartiesAre(FRED);

        analyseDifferences.analyse();

        assertThatNoDifferencesFound();
    }

    @Test
    public void shouldFindANameDifferenceForMatchingPartyWithDifferentName() {
        internalPartiesAre(FRED);
        lastImportedPartiesAre(FREDRICA);

        analyseDifferences.analyse();

        assertThat(analyseDifferences.getDifferences().size(), equalTo(1));
        assertThat(analyseDifferences.getDifferences().get(0).getPropertyDifferences().get(0).getHeader(),
                equalTo("name"));
    }

    @Test
    public void shouldFindASingleNameDifferenceForMatchingPartyWithDifferentNameAndAnAdditionalUnrelatedInternalParty() {
        internalPartiesAre(FRED, GEORGE);
        lastImportedPartiesAre(FREDRICA);

        analyseDifferences.analyse();

        assertThat(analyseDifferences.getDifferences().size(), equalTo(1));
    }

    @Test
    public void shouldNotDetectADifferenceIfBothPropertiesAreNull() {
        internalPartiesAre(NOMAN);
        lastImportedPartiesAre(NOMAN);

        analyseDifferences.analyse();

        assertThatNoDifferencesFound();
    }

    @Test
    public void newPartiesAreDifferences() {
        internalPartiesAre();
        lastImportedPartiesAre(FRED);

        analyseDifferences.analyse();

        assertThat(analyseDifferences.getDifferences().size(), equalTo(1));
        assertThat(analyseDifferences.getDifferences().get(0).isNew(), equalTo(true));
    }

    private void internalPartiesAre(Party... parties) {
        Map<String, Party> result = new HashMap<String, Party>();
        for (Party party : parties) {
            result.put(party.getId(), party);
        }
        when(mockDatabase.getInternalParties()).thenReturn(result);
    }

    private void lastImportedPartiesAre(Party... parties) {
        Map<String, Party> result = new HashMap<String, Party>();
        for (Party party : parties) {
            result.put(party.getId(), party);
        }
        when(mockDatabase.getLastImportParties()).thenReturn(result);
    }

    private Party createParty(String id, String name) {
        Party party = new Party(id);
        party.setProperty("name", name);
        return party;
    }

    private void assertThatNoDifferencesFound() {
        assertThat(analyseDifferences.getDifferences().size(), equalTo(0));
    }

}
