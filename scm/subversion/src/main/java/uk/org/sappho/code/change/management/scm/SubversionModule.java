package uk.org.sappho.code.change.management.scm;

import com.google.inject.AbstractModule;

public class SubversionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SCM.class).to(Subversion.class);
    }
}
