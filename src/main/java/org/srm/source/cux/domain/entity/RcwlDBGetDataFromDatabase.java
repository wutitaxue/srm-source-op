package org.srm.source.cux.domain.entity;

import lombok.Data;

@Data
public class RcwlDBGetDataFromDatabase {
    private String quotationHeaderId;
    private String supplierCompanyName;
    private String companyNum;
    private String supplierCompanyIp;
    private String technologyScore;
    private String businessScore;
    private String score;
    private String scoreRank;
    private String totalAmount;
}
