package uk.org.sappho.codeheatmap.ui.web.server.database;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.org.sappho.codeheatmap.ui.web.server.database.Database;
import uk.org.sappho.codeheatmap.ui.web.server.database.LuceneWrapper;
import uk.org.sappho.codeheatmap.ui.web.shared.model.Party;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseTest {

    private final Party MR_A = createParty("1", "A");
    private final Party MR_B = createParty("2", "B");

    @Mock
    private LuceneWrapper mockLuceneWrapper;

    @Test
    public void shouldSort() {
        Database database = new Database(mockLuceneWrapper);
        database.setInternalParties(asList(MR_B, MR_A));

        List<Party> sortedInternalParties = database.getSortedInternalParties("name", Database.Sort.ASC);

        assertThat(sortedInternalParties.get(0), equalTo(MR_A));
        assertThat(sortedInternalParties.get(1), equalTo(MR_B));

    }

    private Party createParty(String id, String name) {
        Party party = new Party(id);
        party.setProperty("name", name);
        return party;
    }
}
