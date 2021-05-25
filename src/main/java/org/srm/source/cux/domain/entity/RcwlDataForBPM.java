package org.srm.source.cux.domain.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import javax.rmi.CORBA.Util;
import java.util.List;

@Data
public class RcwlDataForBPM {
    @JsonProperty("FSUBJECT")
    private String FSUBJECT;
    @JsonProperty("SUBMITTEDBY")
    private String SUBMITTEDBY;
    @JsonProperty("TITLE")
    private String TITLE;
    @JsonProperty("FILE")
    private String FILE;
    @JsonProperty("SOURCENUM")
    private String SOURCENUM;
    @JsonProperty("SOURCENAME")
    private String SOURCENAME;
    @JsonProperty("CLARIFYNUM")
    private String CLARIFYNUM;
    @JsonProperty("ROUNDNUMBER")
    private String ROUNDNUMBER;
    @JsonProperty("CLARIFYNUMBER")
    private String CLARIFYNUMBER;
    @JsonProperty("CONTEXT")
    private String CONTEXT;
    @JsonProperty("URL_MX")
    private String URL_MX;
    @JsonProperty("FKGLYSD")
    private List<RcwlAttachmentListDataForBPM> ATTACHMENTS;

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{\"FSUBJECT\":\"").append(this.getFSUBJECT()).append("\",");
        sb.append("\"SUBMITTEDBY\":\"").append(this.getSUBMITTEDBY()).append("\",");
        sb.append("\"TITLE\":\"").append(this.getTITLE()).append("\",");
        sb.append("\"FILE\":\"").append(this.getTITLE()).append("\",");
        sb.append("\"SOURCENUM\":\"").append(this.getSOURCENUM()).append("\",");
        sb.append("\"SOURCENAME\":\"").append(this.getSOURCENAME()).append("\",");
        sb.append("\"CLARIFYNUM\":\"").append(this.getCLARIFYNUM()).append("\",");
        sb.append("\"ROUNDNUMBER\":\"").append(this.getROUNDNUMBER()).append("\",");
        sb.append("\"CLARIFYNUMBER\":\"").append(this.getCLARIFYNUMBER()).append("\",");
        String newCONTEXT = this.getCONTEXT();
        if(newCONTEXT.contains("\n")){
            newCONTEXT = newCONTEXT.replace("\n","\\n");
        }
        sb.append("\"CONTEXT\":\"").append(newCONTEXT).append("\",");
        sb.append("\"URL_MX\":\"").append(this.getURL_MX()).append("\",");
        sb.append("\"FKGLYSD\":[");
        if(!CollectionUtils.isEmpty(this.getATTACHMENTS()) && this.getATTACHMENTS().size()>0){
            for(RcwlAttachmentListDataForBPM ra : this.getATTACHMENTS()){
                sb.append(ra.toString()).append(",");
            }
            sb.deleteCharAt(sb.length()-1);
        }
            sb.append("]}");
        return  sb.toString();
    }

}
