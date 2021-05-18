package org.srm.source.cux.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CalibrationApprovalDbdbjgDataForBPM {
    @JsonProperty("SECTIONNAME")
    private String SECTIONNAME;
    @JsonProperty("SUPPLIERCOMPANYNUM")
    private String SUPPLIERCOMPANYNUM;
    @JsonProperty("IP")
    private String IP;
    @JsonProperty("APPENDREMARK")
    private String APPENDREMARK;
    @JsonProperty("TECHNICALSCORE")
    private String TECHNICALSCORE;
    @JsonProperty("BUSINESSSCORE")
    private String BUSINESSSCORE;
    @JsonProperty("COMPREHENSIVE")
    private String COMPREHENSIVE;
    @JsonProperty("COMPREHENSIVERANK")
    private String COMPREHENSIVERANK;
    @JsonProperty("BIDPRICE")
    private String BIDPRICE;
    @JsonProperty("FIXEDPRICE")
    private String FIXEDPRICE;
    @JsonProperty("REMARKS")
    private String REMARKS;

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{\"SECTIONNAME\":\"").append(this.getSECTIONNAME()).append("\",");
        sb.append("\"SUPPLIERCOMPANYNUM\":\"").append(this.getSUPPLIERCOMPANYNUM()).append("\",");
        sb.append("\"IP\":\"").append(this.getIP()).append("\",");
        sb.append("\"APPENDREMARK\":\"").append(this.getAPPENDREMARK()).append("\",");
        sb.append("\"TECHNICALSCORE\":\"").append(this.getTECHNICALSCORE()).append("\",");
        sb.append("\"BUSINESSSCORE\":\"").append(this.getBUSINESSSCORE()).append("\",");
        sb.append("\"COMPREHENSIVE\":\"").append(this.getCOMPREHENSIVE()).append("\",");
        sb.append("\"COMPREHENSIVERANK\":\"").append(this.getCOMPREHENSIVERANK()).append("\",");
        sb.append("\"BIDPRICE\":\"").append(this.getBIDPRICE()).append("\",");
        sb.append("\"FIXEDPRICE\":\"").append(this.getFIXEDPRICE()).append("\",");
        sb.append("\"REMARKS\":\"").append(this.getREMARKS()).append("\"}");
        return  sb.toString();
    }
}
