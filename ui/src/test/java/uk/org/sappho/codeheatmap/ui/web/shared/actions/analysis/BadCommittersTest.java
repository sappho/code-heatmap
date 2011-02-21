package uk.org.sappho.codeheatmap.ui.web.shared.actions.analysis;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import uk.org.sappho.codeheatmap.ui.web.server.handlers.analysis.BadCommitters;

public class BadCommittersTest {

    private BadCommitters pad;

    @Before
    public void setup() {
        pad = new BadCommitters();
    }

    @Test
    public void firstCommitToAFile() {
        pad.addChangeSet(asList("package/fileA.java"), new Date(), "joe", "ABC-1", BadCommitters.CHANGE);

        assertThat(pad.getCommitters().contains("joe"), is(true));
        assertThat(pad.getFilesCreatedOrChanged("joe"), is(1));
        assertThat(pad.getDefectsCaused("joe"), is(0));
    }

    @Test
    public void secondCommitToAFileThatFixesADefect() {
        pad.addChangeSet(asList("package/fileA.java"), new Date(), "joe", "ABC-1", BadCommitters.CHANGE);
        pad.addChangeSet(asList("package/fileA.java"), new Date(), "fred", "ABC-2", BadCommitters.DEFECT);

        assertThat(pad.getCommitters().contains("joe"), is(true));
        assertThat(pad.getCommitters().contains("fred"), is(true));
        assertThat(pad.getFilesCreatedOrChanged("joe"), is(1));
        assertThat(pad.getFilesCreatedOrChanged("fred"), is(0));
        assertThat(pad.getDefectsCaused("joe"), is(1));
    }

    @Test
    public void twoCommitsByDifferentPeopleThatAreBothChanges() {
        pad.addChangeSet(asList("package/fileA.java"), new Date(), "joe", "ABC-1", BadCommitters.CHANGE);
        pad.addChangeSet(asList("package/fileA.java"), new Date(), "fred", "ABC-1", BadCommitters.CHANGE);

        assertThat(pad.getFilesCreatedOrChanged("joe"), is(1));
        assertThat(pad.getFilesCreatedOrChanged("fred"), is(0));
        assertThat(pad.getDefectsCaused("joe"), is(0));
        assertThat(pad.getDefectsCaused("fred"), is(0));
    }

    @Test
    public void onlyScoreOncePerDefect() {
        pad.addChangeSet(asList("package/fileA.java"), new Date(), "joe", "ABC-1", BadCommitters.CHANGE);
        pad.addChangeSet(asList("package/fileA.java"), new Date(), "fred", "ABC-2", BadCommitters.DEFECT);
        pad.addChangeSet(asList("package/fileA.java"), new Date(), "john", "ABC-2", BadCommitters.DEFECT);

        assertThat(pad.getFilesCreatedOrChanged("joe"), is(1));
        assertThat(pad.getFilesCreatedOrChanged("fred"), is(0));
        assertThat(pad.getFilesCreatedOrChanged("john"), is(0));
        assertThat(pad.getDefectsCaused("joe"), is(1));
        assertThat(pad.getDefectsCaused("fred"), is(0));
        assertThat(pad.getDefectsCaused("john"), is(0));
    }

    @Test
    public void canGetListOfStats() {
        pad.getList();
    }
}
