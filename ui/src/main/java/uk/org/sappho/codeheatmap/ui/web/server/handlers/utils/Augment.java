package uk.org.sappho.codeheatmap.ui.web.server.handlers.utils;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RawData;
import uk.org.sappho.code.change.management.data.RevisionData;
import uk.org.sappho.codeheatmap.ui.web.server.handlers.AugmentedRevisionData;
import ch.lambdaj.function.convert.Converter;

public class Augment {

    public static Converter<RevisionData, AugmentedRevisionData> intoAugmentedRevisions(final RawData rawData) {
        return new Converter<RevisionData, AugmentedRevisionData>() {
            @Override
            public AugmentedRevisionData convert(RevisionData from) {
                AugmentedRevisionData aug = new AugmentedRevisionData(
                        from.getIssueKey(),
                        from.getDate(),
                        from.getCommitComment(),
                        from.getCommitter(),
                        from.getChangedFiles(),
                        from.getIssueKey());
                IssueData issueData = rawData.getIssueData(aug.getIssueKey());
                if (issueData != null) {
                    aug.setMainRelease(issueData.getMainRelease());
                    aug.setType(issueData.getType());
                }
                return aug;
            }
        };
    }

}
