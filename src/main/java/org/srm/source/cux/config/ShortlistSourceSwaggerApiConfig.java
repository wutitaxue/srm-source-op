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

    @Autowired
    public ShortlistSourceSwaggerApiConfig(Docket docket) {
        docket.tags(
                new Tag(SOURCE_RFX_SHORTLIST, "寻源入围单")
        );
    }
}
