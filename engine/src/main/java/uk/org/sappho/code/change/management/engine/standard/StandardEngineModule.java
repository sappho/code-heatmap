package uk.org.sappho.code.change.management.engine.standard;

import com.google.inject.AbstractModule;

import uk.org.sappho.code.change.management.engine.Engine;

public class StandardEngineModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Engine.class).to(StandardEngine.class);
    }
}
