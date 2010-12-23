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

        log.debug("===================================================================");
        log.debug("Setting up blank warnings list");
        simpleWarningList = new SimpleWarningList();
    }

    @Test
    public void shouldValidate() {

        log.debug("===================================================================");
        log.debug("Running shouldValidate");
        assertTrue(simpleWarningList.isValid());
        simpleWarningList.add("Test 1", "Example warning");
        simpleWarningList.add("Test 1", "Another example warning");
        assertTrue(simpleWarningList.isValid());
        assertTrue(simpleWarningList.getCategories().size() == 1);
        assertTrue(simpleWarningList.getWarnings("Test 1").size() == 2);
        simpleWarningList.add("Test 2", "Another example warning in a new category");
        assertTrue(simpleWarningList.isValid());
        assertTrue(simpleWarningList.getCategories().size() == 2);
        assertTrue(simpleWarningList.getWarnings("Test 1").size() == 2);
        assertTrue(simpleWarningList.getWarnings("Test 2").size() == 1);
    }

    @Test
    public void shouldFailValidationDueToNullCategory() {

        log.debug("===================================================================");
        log.debug("Running shouldFailValidationDueToNullCategory");
        simpleWarningList.add(null, "Example warning");
        assertFalse(simpleWarningList.isValid());
    }

    @Test
    public void shouldFailValidationDueToEmptyWarning() {

        log.debug("===================================================================");
        log.debug("Running shouldFailValidationDueToEmptyWarning");
        simpleWarningList.add("Empty test", "");
        assertFalse(simpleWarningList.isValid());
    }

    @Test
    public void shouldFailValidationDueToEmptyCategoryAndWarning() {

        log.debug("===================================================================");
        log.debug("Running shouldFailValidationDueToEmptyCategoryAndWarning");
        simpleWarningList.add("", "");
        assertFalse(simpleWarningList.isValid());
    }

    @Test
    public void shouldFailValidationDueToNullCategoryAndWarning() {

        log.debug("===================================================================");
        log.debug("Running shouldFailValidationDueToNullCategoryAndWarning");
        simpleWarningList.add(null, null);
        assertFalse(simpleWarningList.isValid());
    }

    @Test
    public void shouldFailValidationDueToMixedFaults() {

        log.debug("===================================================================");
        log.debug("Running shouldFailValidationDueToMixedFaults");
        simpleWarningList.add("Test 1", "Example warning");
        simpleWarningList.add(null, null);
        simpleWarningList.add("Test 1", "Another example warning");
        simpleWarningList.add(null, "Example warning");
        simpleWarningList.add("", "Warning with blank category");
        simpleWarningList.add("Test 2", "Another example warning in a new category");
        simpleWarningList.add("Empty test", "");
        assertFalse(simpleWarningList.isValid());
    }
}
