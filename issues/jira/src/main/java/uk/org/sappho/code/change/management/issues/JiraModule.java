package uk.org.sappho.code.change.management.issues;

import com.google.inject.AbstractModule;

public class JiraModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(IssueManagement.class).to(Jira.class);
    }
}
