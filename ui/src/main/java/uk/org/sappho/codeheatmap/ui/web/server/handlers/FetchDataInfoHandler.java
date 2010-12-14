package uk.org.sappho.codeheatmap.ui.web.server.handlers;

import static java.util.Arrays.asList;

import java.util.List;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;

import com.google.inject.Inject;

import uk.org.sappho.codeheatmap.ui.web.server.database.Database;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchDataInfo;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchDataInfoResult;
import uk.org.sappho.codeheatmap.ui.web.shared.model.DataInfo;

public class FetchDataInfoHandler implements ActionHandler<FetchDataInfo, FetchDataInfoResult> {

    private final Database database;

    @Inject
    public FetchDataInfoHandler(Database database) {
        this.database = database;
    }

    @Override
    public Class<FetchDataInfo> getActionType() {
        return FetchDataInfo.class;
    }

    @Override
    public FetchDataInfoResult execute(FetchDataInfo action, ExecutionContext context)
            throws DispatchException {

        List<DataInfo> dataInfo = asList(
                new DataInfo("Total parties", String.valueOf(database.getLastImportParties().size())),
                new DataInfo("New parties", String.valueOf(database.getLastImportParties().size()
                        - database.getInternalParties().size()))
                );
        return new FetchDataInfoResult(dataInfo);
    }

    @Override
    public void rollback(FetchDataInfo action, FetchDataInfoResult result, ExecutionContext context)
            throws DispatchException {

    }

}
