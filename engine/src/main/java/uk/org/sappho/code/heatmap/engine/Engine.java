package uk.org.sappho.code.heatmap.engine;

import java.util.List;

public interface Engine {

    public void add(List<ChangeSet> changeSets);

    public void run() throws EngineException;
}
