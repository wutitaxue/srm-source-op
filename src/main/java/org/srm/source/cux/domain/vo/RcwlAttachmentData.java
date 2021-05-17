package org.srm.source.cux.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RcwlAttachmentData {

    //序号
    @JsonProperty(value = "FILENUMBER")
    private String FileNumber;

    //名称
    @JsonProperty(value = "FILENAME")
    private String FileName;

    //大小
    @JsonProperty(value = "FILESIZE")
    private String FileSize;

    //名称
    @JsonProperty(value = "DESCRIPTION")
    private String Description;

    //附件URL
    @JsonProperty(value = "URL")
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
