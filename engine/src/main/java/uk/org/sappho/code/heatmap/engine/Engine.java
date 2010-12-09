package uk.org.sappho.code.heatmap.engine;

import java.util.List;

import uk.org.sappho.code.change.management.data.ChangeSet;

public interface Engine {

    public void add(List<ChangeSet> changeSets);

    public void run() throws EngineException;
}
