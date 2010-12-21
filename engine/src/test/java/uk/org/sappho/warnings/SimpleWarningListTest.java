package uk.org.sappho.warnings;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class SimpleWarningListTest {

    private SimpleWarningList simpleWarningList;

    private static final Logger log = Logger.getLogger(SimpleWarningListTest.class);

    @Before
    public void setup() {

        log.info("Setting up blank warnings list");
        simpleWarningList = new SimpleWarningList();
    }

    @Test
    public void shouldValidate() {

        assertTrue(simpleWarningList.isValid());
        simpleWarningList.add("Test 1", "Example warning");
        simpleWarningList.add("Test 1", "Another example warning", false);
        assertTrue(simpleWarningList.isValid());
        assertTrue(simpleWarningList.getCategories().size() == 1);
        assertTrue(simpleWarningList.getWarnings("Test 1").size() == 2);
        simpleWarningList.add("Test 2", "Another example warning in a new category", false);
        assertTrue(simpleWarningList.isValid());
        assertTrue(simpleWarningList.getCategories().size() == 2);
        assertTrue(simpleWarningList.getWarnings("Test 1").size() == 2);
        assertTrue(simpleWarningList.getWarnings("Test 2").size() == 1);
    }

    @Test
    public void shouldFailValidationDueToNullCategory() {

        simpleWarningList.add(null, "Example warning");
        assertFalse(simpleWarningList.isValid());
    }

    @Test
    public void shouldFailValidationDueToEmptyWarning() {

        simpleWarningList.add("Empty test", "");
        assertFalse(simpleWarningList.isValid());
    }

    @Test
    public void shouldFailValidationDueToEmptyCategoryAndWarning() {

        simpleWarningList.add("", "");
        assertFalse(simpleWarningList.isValid());
    }

    @Test
    public void shouldFailValidationDueToNullCategoryAndWarning() {

        simpleWarningList.add(null, null);
        assertFalse(simpleWarningList.isValid());
    }
}
