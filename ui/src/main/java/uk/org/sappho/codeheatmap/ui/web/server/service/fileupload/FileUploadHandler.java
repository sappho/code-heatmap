package uk.org.sappho.codeheatmap.ui.web.server.service.fileupload;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class FileUploadHandler extends UploadAction {

    private static final long serialVersionUID = -8459441719275291193L;

    @Inject
    public FileUploadHandler() {
        maxSize = DEFAULT_REQUEST_LIMIT_KB;
        uploadDelay = 0;
    }

    @Override
    public String executeAction(HttpServletRequest request,
            List<FileItem> sessionFiles) throws UploadActionException {

        try {
            for (FileItem fileItem : sessionFiles) {
            }
        } finally {
            removeSessionFileItems(request);
        }
        return "SUCCESS";
    }
}