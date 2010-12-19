package uk.org.sappho.codeheatmap.ui.web.shared.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class HeaderMatchTest {

    @Test
    public void shouldSort() {
        assertThat(new HeaderMatch("a", "a").compareTo(null), lessThan(0));
        assertThat(new HeaderMatch("a", "a").compareTo(new HeaderMatch("a", "a")), equalTo(0));

        assertThat(new HeaderMatch("a", "a").compareTo(new HeaderMatch(null, "a")), lessThan(0));
        assertThat(new HeaderMatch("a", "a").compareTo(new HeaderMatch("a", null)), lessThan(0));
        assertThat(new HeaderMatch("a", "a").compareTo(new HeaderMatch(null, null)), lessThan(0));

        assertThat(new HeaderMatch(null, "a").compareTo(new HeaderMatch(null, "a")), equalTo(0));

        assertThat(new HeaderMatch("a", "a").compareTo(new HeaderMatch("b", "b")), lessThan(0));
    }
}
