package uk.org.sappho.code.heatmap.basic;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import uk.org.sappho.code.change.management.data.IssueData;
import uk.org.sappho.code.change.management.data.RevisionData;
import uk.org.sappho.code.heatmap.weight.WeightCalculator;
import uk.org.sappho.configuration.Configuration;

@Singleton
public class BasicWeightCalculator implements WeightCalculator {

    private final Configuration config;
    private final Map<String, Integer> weights = new HashMap<String, Integer>();
    private static final Logger log = Logger.getLogger(BasicWeightCalculator.class);

    @Inject
    public BasicWeightCalculator(Configuration config) {

        log.info("Using basic heat map item weight calculator");
        this.config = config;
    }

    public int getWeight(Collection<RevisionData> revisions, Collection<IssueData> issues) {

        int weight = 0;
        for (IssueData issue : issues) {
            String type = issue.getType();
            Integer weightForType = weights.get(type);
            if (weightForType == null) {
                weightForType = Integer.parseInt(config.getProperty("weight." + type, "1"));
                weights.put(type, weightForType);
            }
            weight += weightForType;
        }
        return weight;
    }
}
