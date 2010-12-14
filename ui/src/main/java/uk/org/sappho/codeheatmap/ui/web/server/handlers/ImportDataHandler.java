package uk.org.sappho.codeheatmap.ui.web.server.handlers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;

import com.google.inject.Inject;

import uk.org.sappho.codeheatmap.ui.web.server.database.Database;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.ImportData;
import uk.org.sappho.codeheatmap.ui.web.shared.actions.ImportDataResult;
import uk.org.sappho.codeheatmap.ui.web.shared.model.HeaderMatch;
import uk.org.sappho.codeheatmap.ui.web.shared.model.Party;
import uk.org.sappho.codeheatmap.ui.web.shared.model.PartyKeyProvider;

public class ImportDataHandler implements ActionHandler<ImportData, ImportDataResult> {

    private final Database database;
    private final PartyKeyProvider partyKeyProvider;

    @Inject
    public ImportDataHandler(Database database, PartyKeyProvider partyKeyProvider) {
        this.database = database;
        this.partyKeyProvider = partyKeyProvider;
    }

    @Override
    public Class<ImportData> getActionType() {
        return ImportData.class;
    }

    @Override
    public ImportDataResult execute(ImportData action, ExecutionContext context) throws DispatchException {

        Map<String, Party> lastImportParties = database.getLastImportParties();
        List<HeaderMatch> headerNameMatches = action.getHeaderNameMatches();

        List<Party> newParties = new ArrayList<Party>();
        Set<String> newHeaderNames = new HashSet<String>();
        for (Party party : lastImportParties.values()) {
            Party newParty = new Party(partyKeyProvider.getKey(party));
            for (HeaderMatch headerMatch : headerNameMatches) {
                if (headerMatch.isSelected()) {
                    newParty.setProperty(headerMatch.getLastImport(), party.getProperty(headerMatch.getLastImport()));
                    newHeaderNames.add(headerMatch.getLastImport());
                }
            }
            newParties.add(newParty);
        }
        database.setInternalParties(newParties);
        database.saveInternalHeaderNames(newHeaderNames);
        return new ImportDataResult();
    }

    @Override
    public void rollback(ImportData action, ImportDataResult result, ExecutionContext context) throws DispatchException {

    }

}
