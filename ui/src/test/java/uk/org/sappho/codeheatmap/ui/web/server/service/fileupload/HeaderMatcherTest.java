package uk.org.sappho.codeheatmap.ui.web.server.service.fileupload;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.org.sappho.codeheatmap.ui.web.server.database.Database;
import uk.org.sappho.codeheatmap.ui.web.server.service.fileupload.HeaderMatcher;

@RunWith(MockitoJUnitRunner.class)
public class HeaderMatcherTest {

    @Mock
    private Database mockDatabase;
    private HeaderMatcher headerMatcher;

    @Before
    public void setupSut() {
        headerMatcher = new HeaderMatcher(mockDatabase);
    }

    @Test
    public void shouldMatchNothingForEmptyLists() {

        internalHeadersAre(emptyHeaders());
        lastImportHeaderAre(emptyHeaders());

        headerMatcher.match();

        assertThat(headerMatcher.getHeaderMatches().size(), equalTo(0));
    }

    @Test
    public void shouldNotMatchInternalWithNoLastCommitted() {

        internalHeadersAre(asList("header1"));
        lastImportHeaderAre(emptyHeaders());

        headerMatcher.match();

        assertThat(headerMatcher.getHeaderMatches().size(), equalTo(1));
        assertThat(headerMatcher.getHeaderMatches().get(0).getInternal(), equalTo("header1"));
        assertThat(headerMatcher.getHeaderMatches().get(0).getLastImport(), equalTo(null));
    }

    @Test
    public void shouldNotMatchLastCommittedWithNoInternal() {

        internalHeadersAre(emptyHeaders());
        lastImportHeaderAre(asList("header1"));

        headerMatcher.match();

        assertThat(headerMatcher.getHeaderMatches().size(), equalTo(1));
        assertThat(headerMatcher.getHeaderMatches().get(0).getLastImport(), equalTo("header1"));
        assertThat(headerMatcher.getHeaderMatches().get(0).getInternal(), equalTo(null));
    }

    @Test
    public void shouldMatchOneOfEach() {

        internalHeadersAre(asList("header1"));
        lastImportHeaderAre(asList("header1"));

        headerMatcher.match();

        assertThat(headerMatcher.getHeaderMatches().size(), equalTo(1));
        assertThat(headerMatcher.getHeaderMatches().get(0).getLastImport(), equalTo("header1"));
        assertThat(headerMatcher.getHeaderMatches().get(0).getInternal(), equalTo("header1"));
    }

    @Test
    public void oneOfEachMoreComplexCombination() {
        internalHeadersAre(asList("header1", "unique1"));
        lastImportHeaderAre(asList("header1", "unique2"));

        headerMatcher.match();

        assertThat(headerMatcher.getHeaderMatches().size(), equalTo(3));

    }

    @Test
    public void matchesShouldBeSelected() {
        internalHeadersAre(asList("header1", "unique1"));
        lastImportHeaderAre(asList("header1", "unique2"));

        headerMatcher.match();

        assertThat(headerMatcher.getHeaderMatches().size(), equalTo(3));
        assertThat(headerMatcher.getHeaderMatches().get(0).isSelected(), equalTo(true));
        assertThat(headerMatcher.getHeaderMatches().get(1).isSelected(), equalTo(false));
        assertThat(headerMatcher.getHeaderMatches().get(2).isSelected(), equalTo(false));

    }

    private void lastImportHeaderAre(List<String> lastImportHeaders) {
        when(mockDatabase.getLastImportHeaderNames()).thenReturn(new HashSet<String>(lastImportHeaders));
    }

    private void internalHeadersAre(List<String> internalHeaders) {
        when(mockDatabase.getInternalHeaderNames()).thenReturn(new HashSet<String>(internalHeaders));
    }

    private List<String> emptyHeaders() {
        return Collections.<String> emptyList();
    }
}
