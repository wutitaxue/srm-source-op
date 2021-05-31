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
        sb.append("{\"FSubject\":\"").append(this.getFSUBJECT()).append("\",");
        sb.append("\"submittedby\":\"").append(this.getSUBMITTEDBY()).append("\",");
        sb.append("\"title\":\"").append(this.getTITLE()).append("\",");
        sb.append("\"file\":\"").append("答疑").append("\",");
        sb.append("\"sourcenum\":\"").append(this.getSOURCENUM()).append("\",");
        sb.append("\"sourcename\":\"").append(this.getSOURCENAME()).append("\",");
        sb.append("\"clarifynum\":\"").append(this.getCLARIFYNUM()).append("\",");
        sb.append("\"ROUNDNUMBER\":\"").append(this.getROUNDNUMBER()).append("\",");
        sb.append("\"clarifynumber\":\"").append(this.getCLARIFYNUMBER()).append("\",");
        String newCONTEXT = this.getCONTEXT();
        if(null != newCONTEXT && !"".equals(newCONTEXT)){
            newCONTEXT = newCONTEXT.replace("\n","");
            newCONTEXT = newCONTEXT.replace("<p>","");
            newCONTEXT = newCONTEXT.replace("</p>","");
        }
        sb.append("\"context\":\"").append(newCONTEXT).append("\",");
        sb.append("\"URL_MX\":\"").append(this.getURL_MX()).append("\",");
        sb.append("\"ATTACHMENTS1\":[");
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
