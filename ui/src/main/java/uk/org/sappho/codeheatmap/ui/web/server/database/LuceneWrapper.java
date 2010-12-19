package uk.org.sappho.codeheatmap.ui.web.server.database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import com.google.inject.Singleton;

import uk.org.sappho.codeheatmap.ui.web.shared.model.Party;

@Singleton
public class LuceneWrapper {

    private final RAMDirectory directory = new RAMDirectory();
    private final StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);

    public LuceneWrapper() {
    }

    public void save(Party party) {
        try {
            IndexWriter indexWriter = new IndexWriter(
                    directory,
                    analyzer,
                    MaxFieldLength.UNLIMITED);
            Document doc = new Document();
            for (String property : party.getPropertyNames()) {
                String value = party.getProperty(property);
                if (value != null) {
                    doc.add(new Field(property, value, Store.NO, Index.ANALYZED));
                }
            }
            doc.add(new Field("id", party.getId(), Store.YES, Index.NO));
            indexWriter.addDocument(doc);
            indexWriter.optimize();
            indexWriter.close();
        } catch (CorruptIndexException e) {
            throw new RuntimeException("failed to save party", e);
        } catch (IOException e) {
            throw new RuntimeException("failed to save party", e);
        }
    }

    public List<String> findPartyIdsByTerm(String searchTerm) {
        try {
            IndexSearcher searcher = new IndexSearcher(directory);
            Query query = new QueryParser(Version.LUCENE_30, "BusinessName", analyzer).parse(searchTerm);
            TopDocs topDocs = searcher.search(query, 100);
            List<String> results = new ArrayList<String>();
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                String partyId = doc.get("id");
                results.add(partyId);
            }
            return results;
        } catch (CorruptIndexException e) {
            throw new RuntimeException("failed to find party", e);
        } catch (IOException e) {
            throw new RuntimeException("failed to find party", e);
        } catch (ParseException e) {
            throw new RuntimeException("failed to find party", e);
        }
    }
}
