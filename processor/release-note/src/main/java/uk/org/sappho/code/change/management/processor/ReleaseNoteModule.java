package uk.org.sappho.code.change.management.processor;

import com.google.inject.AbstractModule;

import uk.org.sappho.code.change.management.data.processing.RawDataProcessing;

public class ReleaseNoteModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(RawDataProcessing.class).to(ReleaseNote.class);
    }
}
