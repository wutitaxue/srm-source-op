package org.srm.source.cux.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RcwlRfxHeaderAttachmentListDataForBPM {
    @JsonProperty("FILENUMBER")
    private String FILENUMBER;
    @JsonProperty("FILENAME")
    private String FILENAME;
    @JsonProperty("FILESIZE")
    private String FILESIZE;
    @JsonProperty("DESCRIPTION")
    private String DESCRIPTION;
    @JsonProperty("URL")
    private String URL;

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{\"FILENUMBER\":\"").append(this.getFILENUMBER()).append("\",");
        sb.append("\"FILENAME\":\"").append(this.getFILENAME()).append("\",");
        sb.append("\"FILESIZE\":\"").append(this.getFILESIZE()).append("\",");
        sb.append("\"DESCRIPTION\":\"").append(this.getDESCRIPTION()).append("\",");
        sb.append("\"URL\":\"").append(this.getURL()).append("\"}");
        return  sb.toString();
    }
}
