package org.srm.source.cux.rfx.api.controller.config;

import org.springframework.beans.factory.annotation.Autowired;
import springfox.documentation.service.Tag;
import springfox.documentation.spring.web.plugins.Docket;

public class TransactionSwaggerApiConfig {

    public static final String RFX_SUPPLIER_QUERY = "Rfx SupplierQuery";

    @Autowired
    public TransactionSwaggerApiConfig(Docket docket) {
        docket.tags(new Tag(RFX_SUPPLIER_QUERY, "供应商360查询"));
    }
}
