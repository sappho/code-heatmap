package uk.org.sappho.codeheatmap.ui.web.server.handlers;

import java.util.ArrayList;
import java.util.List;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.DataItem;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchData;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.FetchDataResult;

import com.google.inject.Inject;

public class FetchDataHandler implements ActionHandler<FetchData, FetchDataResult> {

    @Inject
    public FetchDataHandler() {
    }

    @Override
    public Class<FetchData> getActionType() {
        return FetchData.class;
    }

    @Override
    public FetchDataResult execute(FetchData action, ExecutionContext context)
            throws DispatchException {

        List<DataItem> data = new ArrayList<DataItem>();
        data.add(new DataItem("9.01", 20, 50));
        data.add(new DataItem("9.02", 30, 60));
        data.add(new DataItem("9.03", 25, 80));
        data.add(new DataItem("9.04", 30, 70));
        data.add(new DataItem("9.05", 15, 30));
        data.add(new DataItem("9.06", 10, 30));
        return new FetchDataResult(data);
    }

    @Override
    public void rollback(FetchData action, FetchDataResult result, ExecutionContext context)
            throws DispatchException {
    }

}
