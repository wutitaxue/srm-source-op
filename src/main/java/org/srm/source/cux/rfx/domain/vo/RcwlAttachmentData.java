package org.srm.source.cux.rfx.domain.vo;


import com.alibaba.fastjson.annotation.JSONField;

public class RcwlAttachmentData {

    //序号
    @JSONField(name = "FILENUMBER")
    private String FileNumber;

    //名称
    @JSONField(name = "FILENAME")
    private String FileName;

    //大小
    @JSONField(name = "FILESIZE")
    private String FileSize;

    //名称
    @JSONField(name = "DESCRIPTION")
    private String Description;

    //附件URL
    @JSONField(name = "URL")
    private String Url;

    public String getFileNumber() {
        return FileNumber;
    }

    public void setFileNumber(String fileNumber) {
        FileNumber = fileNumber;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFileSize() {
        return FileSize;
    }

    public void setFileSize(String fileSize) {
        FileSize = fileSize;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
