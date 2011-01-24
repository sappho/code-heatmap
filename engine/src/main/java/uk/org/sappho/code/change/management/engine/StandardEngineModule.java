package uk.org.sappho.code.change.management.engine;

import com.google.inject.AbstractModule;

public class StandardEngineModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Engine.class).to(StandardEngine.class);
    }
}
