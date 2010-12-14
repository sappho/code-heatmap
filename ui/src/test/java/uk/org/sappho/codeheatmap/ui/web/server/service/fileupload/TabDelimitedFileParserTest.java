package uk.org.sappho.codeheatmap.ui.web.server.service.fileupload;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.org.sappho.codeheatmap.ui.web.server.service.fileupload.TabDelimitedFileParser;
import uk.org.sappho.codeheatmap.ui.web.shared.model.PartyKeyProvider;

@RunWith(MockitoJUnitRunner.class)
public class TabDelimitedFileParserTest {

    TabDelimitedFileParser parser;
    @Mock
    private PartyKeyProvider mockPartyKeyProvider;

    @Before
    public void setupSut() {
        parser = new TabDelimitedFileParser(mockPartyKeyProvider);
    }

    @Test
    public void shouldParseEmptyFile() {
        parser.parse("");
        assertThatResultIsEmpty();
    }

    @Test
    public void shouldParseFileWithOnlyLineFeed() {
        parser.parse("\n");
        assertThatResultIsEmpty();
    }

    @Test
    public void shouldParseFileWithOnlyCRLF() {
        parser.parse("\r\n");
        assertThatResultIsEmpty();
    }

    @Test
    public void shouldStoreHeaderLine() {
        parser.parse("Accountid\tCountryCode\tDuns\r\n");
        assertThatResultIsEmpty();
        Set<String> headerNames = parser.getHeaderNames();
        assertThat(headerNames.size(), equalTo(3));
    }

    @Test
    public void shouldHandleSingleEntryWithHeaderLFDelimited() {
        parser.parse("Accountid CountryCode Duns    HQDuns  BusinessName    Address1    Address2    Town    PostCode    State   Telephone   Fax BusinessNumber  LedgerCurrency  LedgerOutstanding   LedgerNotYetDue LedgerOverdue   LedgerOverdue1to30  LedgerOverdue31to60 LedgerOverdue61to90 LedgerOverdue91plus LedgerCreditLimit   LedgerCustom1   LedgerKey   DBUpdateDate    DBCurrency1 DBCreditLimit   DBRating    DBFailureScore1 DBPaydex    DBDelinquency   DBOverrideCode  DBEmployees DBLegalForm DBStartYear DBActivity  DBActivityType  DBUltimateDUNS  DBUltimateName  DBUltimateCountry   PMMonitoringStatus  PMLedgerName    DSO CustomRiskScore LedgerCustom2\n"
                + "203610  AU  752122465   752122465   A.I.S. INSURANCE BROKERS PTY. LTD.  137 MORAY ST        SOUTH MELBOURNE 3205    VICTORIA    386998888   386998899   065797597   USD 1.00                                TFRM    Australasia 05/09/2010 12:25:41 USD     -                   0   Public Company  1990    6411    1987    752122465   A.I.S. INSURANCE BROKERS PTY. LTD.  AU  1   Australasia     OTHER   A I S Ins Bkrs Pty Ltd\n");
        assertThatResultSizeIs(1);
    }

    @Test
    public void shouldHandleSingleEntryWithHeaderCRLFDelimited() {
        parser.parse("Accountid\tCountryCode\tDuns\tHQDuns\tBusinessName\tAddress1\tAddress2\tTown\tPostCode\tState\tTelephone\tFax\tBusinessNumber\tLedgerCurrency\tLedgerOutstanding\tLedgerNotYetDue\tLedgerOverdue\tLedgerOverdue1to30  LedgerOverdue31to60 LedgerOverdue61to90 LedgerOverdue91plus LedgerCreditLimit   LedgerCustom1   LedgerKey   DBUpdateDate    DBCurrency1 DBCreditLimit   DBRating    DBFailureScore1 DBPaydex    DBDelinquency   DBOverrideCode  DBEmployees DBLegalForm DBStartYear\tDBActivity\tDBActivity\tDBUltimateDUNS\tDBUltimateName\tDBUltimateCountry\tPMMonitoringStatus\tPMLedgerName\tDSO\tCustomRiskScore\tLedgerCustom2\r\n"
                + "203610  AU  752122465   752122465   A.I.S. INSURANCE BROKERS PTY. LTD.  137 MORAY ST        SOUTH MELBOURNE 3205    VICTORIA    386998888   386998899   065797597   USD 1.00                                TFRM    Australasia 05/09/2010 12:25:41 USD     -                   0   Public Company  1990    6411    1987    752122465   A.I.S. INSURANCE BROKERS PTY. LTD.  AU  1   Australasia     OTHER   A I S Ins Bkrs Pty Ltd\r\n");
        assertThatResultSizeIs(1);
    }

    private void assertThatResultIsEmpty() {
        assertThat("list not empty", parser.getParties().isEmpty(), equalTo(true));
    }

    private void assertThatResultSizeIs(int size) {
        assertThat("incorrect size", parser.getParties().size(), equalTo(size));
    }
}
