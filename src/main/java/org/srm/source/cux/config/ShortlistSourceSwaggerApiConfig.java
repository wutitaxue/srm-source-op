package org.srm.source.cux.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.Tag;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author Allen  2018/12/21
 */
@Configuration
public class ShortlistSourceSwaggerApiConfig {

    public static final String SOURCE_RFX_SHORTLIST = "Rfx Shortlist";

    public static final String RCWL_SHORTLIST_HEADERS = "Rcwl Shortlist Headers";

    public static final String RCWL_SHORTLIST_ATTACHMENTS = "Rcwl Shortlist Attachments";

    public static final String RCWL_SUPPLIER_HEADERS = "Rcwl Supplier Headers";

    public static final String RCWL_SUPPLIER_ATTACHMENTS = "Rcwl Supplier Attachments";


    @Autowired
    public ShortlistSourceSwaggerApiConfig(Docket docket) {
        docket.tags(
                new Tag(SOURCE_RFX_SHORTLIST, "寻源入围单"),
                new Tag(RCWL_SHORTLIST_HEADERS, "新寻源入围单头"),
                new Tag(RCWL_SHORTLIST_ATTACHMENTS, "新寻源入围单头附件"),
                new Tag(RCWL_SUPPLIER_HEADERS, "新寻源入围单供应商头"),
                new Tag(RCWL_SUPPLIER_ATTACHMENTS, "新寻源入围单供应商头附件")
        );
    }
}
