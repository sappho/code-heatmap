package uk.org.sappho.codeheatmap.ui.web.server.handlers.utils;

import java.math.BigDecimal;
import java.util.Comparator;

import uk.org.sappho.code.change.management.data.RevisionData;

public class Sorters {

    public static Comparator<RevisionData> byDate() {
        return new Comparator<RevisionData>() {
            @Override
            public int compare(RevisionData o1, RevisionData o2) {
                if (o1 == null || o2 == null) {
                    return 0;
                }
                if (o1.getDate() == null)
                    return -1;
                if (o2.getDate() == null)
                    return 1;

                return o1.getDate().compareTo(o2.getDate());
            }
        };
    }

    public static Comparator<String> asIfNumbers() {
        return new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1 == null || o1.isEmpty())
                    return -1;
                if (o2 == null || o2.isEmpty())
                    return 1;
                try {
                    BigDecimal o1AsBD = new BigDecimal(o1);
                    BigDecimal o2asBD = new BigDecimal(o2);
                    return o1AsBD.compareTo(o2asBD);
                } catch (NumberFormatException nfe) {
                    return 0;
                }
            }
        };
    }

}
