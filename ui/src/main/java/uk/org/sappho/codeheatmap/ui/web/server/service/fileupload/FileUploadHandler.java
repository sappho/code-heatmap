package uk.org.sappho.codeheatmap.ui.web.server.service.fileupload;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import uk.org.sappho.codeheatmap.ui.web.server.database.Database;

@Singleton
public class FileUploadHandler extends UploadAction {

    private static final long serialVersionUID = -8459441719275291193L;

    private final FileParser fileParser;

    private final Database database;

    @Inject
    public FileUploadHandler(FileParser fileParser, Database database) {
        this.fileParser = fileParser;
        this.database = database;
        maxSize = DEFAULT_REQUEST_LIMIT_KB;
        uploadDelay = 0;
    }

    @Override
    public String executeAction(HttpServletRequest request,
            List<FileItem> sessionFiles) throws UploadActionException {

        try {
            for (FileItem fileItem : sessionFiles) {
                fileParser.parse(fileItem.getString("UTF-8"));
                database.saveLastImportParties(fileParser.getParties());
                database.saveLastImportHeaderNames(fileParser.getHeaderNames());
            }
        } catch (UnsupportedEncodingException e) {
            throw new UploadActionException(e);
        } finally {
            removeSessionFileItems(request);
        }
        return "SUCCESS";
    }
}