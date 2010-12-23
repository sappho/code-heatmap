package uk.org.sappho.validation;

import java.util.List;

import net.sf.oval.ConstraintViolation;

public class Validator extends net.sf.oval.Validator {

    public String getReport(List<ConstraintViolation> violations) {

        ConstraintViolation[] array = new ConstraintViolation[violations.size()];
        return getReport("", "Violation", violations.toArray(array));
    }

    private String getReport(String indent, String prefix, ConstraintViolation[] violations) {

        String report = "";
        for (ConstraintViolation violation : violations) {
            report += "\n" + indent + prefix + ": ";
            try {
                report += violation.getMessage() + " on value: " + violation.getInvalidValue();
            } catch (Throwable t) {
                report += "unknown reason";
            }
            ConstraintViolation[] causes = violation.getCauses();
            if (causes != null)
                report += getReport(indent + " ", "Cause", causes);
        }
        return report;
    }
}
