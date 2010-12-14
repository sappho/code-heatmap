package uk.org.sappho.codeheatmap.ui.web.server.handlers;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import com.google.inject.Inject;

import uk.org.sappho.codeheatmap.ui.web.server.database.Database;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchParties;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchPartiesResult;

public class FetchPartiesHandler implements ActionHandler<FetchParties, FetchPartiesResult> {

    private final Database database;

    @Inject
    public FetchPartiesHandler(Database database) {
        this.database = database;
    }

    @Override
    public Class<FetchParties> getActionType() {
        return FetchParties.class;
    }

    @Override
    public FetchPartiesResult execute(FetchParties action, ExecutionContext context)
            throws ActionException {
        return new FetchPartiesResult(database.findPartiesByTerm(action.getSearchTerm()));
        //        return new FetchPartiesResult(database.getSortedInternalParties("BusinessName", Sort.ASC));
    }

    @Override
    public void rollback(FetchParties action, FetchPartiesResult result,
            ExecutionContext context)
            throws ActionException {

    }

}
