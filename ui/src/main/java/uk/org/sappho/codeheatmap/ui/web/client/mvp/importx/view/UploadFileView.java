package uk.org.sappho.codeheatmap.ui.web.client.mvp.importx.view;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.SingleUploader;

import com.google.gwt.user.client.ui.Composite;

public class UploadFileView extends Composite {

    public UploadFileView() {
        SingleUploader defaultUploader = new SingleUploader();
        defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
        initWidget(defaultUploader);
    }

    private final IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
        @Override
        public void onFinish(IUploader uploader) {
            if (uploader.getStatus() == Status.SUCCESS) {
                // Document doc = XMLParser.parse(uploader.getServerResponse());
                // String size = Utils.getXmlNodeValue(doc, "file-1-size");
                // String type = Utils.getXmlNodeValue(doc, "file-1-type");
                // System.out.println(size + " " + type);
                // System.out.println(uploader.fileUrl());
            }
        }
    };
}
