package org.srm.autoconfigure.source;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Source OP Auto Configuration
 *
 * @author zhongsheng.xu@hand-china.com 2021-03-23 20:18:33
 */
@Configuration
@EnableFeignClients("org.srm.source.cux")
@ComponentScan("org.srm.source.cux")
@EnableSrmSource
public class SrmSourceOPAutoConfiguration {
}
