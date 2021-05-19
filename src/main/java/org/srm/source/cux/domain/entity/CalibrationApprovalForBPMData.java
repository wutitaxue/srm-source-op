package org.srm.source.cux.domain.entity;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CalibrationApprovalForBPMData {
    @JsonProperty("FSUBJECT")
    private String FSUBJECT;
    @JsonProperty("COMPANYID")
    private String COMPANYID;
    @JsonProperty("RFXNAME")
    private String RFXNAME;
    @JsonProperty("RFXNUM")
    private String RFXNUM;
    @JsonProperty("BIDDINGMODE")
    private String BIDDINGMODE;
    @JsonProperty("SHORTLISTCATEGORY")
    private String SHORTLISTCATEGORY;
    @JsonProperty("METHODREMARK")
    private String METHODREMARK;
    @JsonProperty("ATTRIBUTEVARCHAR9")
    private String ATTRIBUTEVARCHAR9;
    @JsonProperty("PROJECTAMOUNT")
    private String PROJECTAMOUNT;
    @JsonProperty("ATTRIBUTEVARCHAR12")
    private String ATTRIBUTEVARCHAR12;
    @JsonProperty("ATTRIBUTEVARCHAR13")
    private String ATTRIBUTEVARCHAR13;
    @JsonProperty("ROUNDNUMBER")
    private String ROUNDNUMBER;
    @JsonProperty("OPINION")
    private String OPINION;
    @JsonProperty("URL_MX")
    private String URL_MX;
    @JsonProperty("DBDBJG")
    private List<CalibrationApprovalDbdbjgDataForBPM> DBDBJGS;
    @JsonProperty("ATTACHMENTS")
    private List<CalibrationApprovalAttachmentDataForBPM> ATTACHMENTS;

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{\"FSUBJECT\":\"").append(this.getFSUBJECT()).append("\",");
        sb.append("\"COMPANYID\":\"").append(this.getCOMPANYID()).append("\",");
        sb.append("\"RFXNAME\":\"").append(this.getRFXNAME()).append("\",");
        sb.append("\"RFXNUM\":\"").append(this.getRFXNUM()).append("\",");
        sb.append("\"BIDDINGMODE\":\"").append(this.getBIDDINGMODE()).append("\",");
        sb.append("\"SHORTLISTCATEGORY\":\"").append(this.getSHORTLISTCATEGORY()).append("\",");
        sb.append("\"METHODREMARK\":\"").append(this.getMETHODREMARK()).append("\",");
        sb.append("\"ATTRIBUTEVARCHAR9\":\"").append(this.getATTRIBUTEVARCHAR9()).append("\",");
        sb.append("\"PROJECTAMOUNT\":\"").append(this.getPROJECTAMOUNT()).append("\",");
        sb.append("\"ATTRIBUTEVARCHAR12\":\"").append(this.getATTRIBUTEVARCHAR12()).append("\",");
        sb.append("\"ATTRIBUTEVARCHAR13\":\"").append(this.getATTRIBUTEVARCHAR12()).append("\",");
        sb.append("\"ROUNDNUMBER\":\"").append(this.getROUNDNUMBER()).append("\",");
        sb.append("\"OPINION\":\"").append(this.getOPINION()).append("\",");
        sb.append("\"URL_MX\":\"").append(this.getURL_MX()).append("\",");
        sb.append("\"DBDBJGS\":[");
        if(!CollectionUtils.isEmpty(this.getDBDBJGS()) && this.getDBDBJGS().size()>0){
            for(CalibrationApprovalDbdbjgDataForBPM db : this.getDBDBJGS()){
                sb.append(db.toString()).append(",");
            }
            sb.deleteCharAt(sb.length()-1);
        }
        sb.append("],");
        sb.append("\"ATTACHMENTS\":[");
        if(!CollectionUtils.isEmpty(this.getATTACHMENTS()) && this.getATTACHMENTS().size()>0){
            for(CalibrationApprovalAttachmentDataForBPM at : this.getATTACHMENTS()){
                sb.append(at.toString()).append(",");
            }
            sb.deleteCharAt(sb.length()-1);
        }
        sb.append("]}");
        return  sb.toString();
    }
}
