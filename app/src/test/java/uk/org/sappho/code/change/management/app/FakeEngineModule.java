package uk.org.sappho.code.change.management.app;

import com.google.inject.AbstractModule;

import uk.org.sappho.code.change.management.engine.Engine;

public class FakeEngineModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Engine.class).to(FakeEngine.class);
    }
}
