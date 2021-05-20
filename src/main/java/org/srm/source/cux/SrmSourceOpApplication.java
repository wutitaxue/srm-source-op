package org.srm.source.cux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.srm.autoconfigure.source.EnableSrmSourceOP;
/**
 * SRM寻源服务应用
 *
 * @author zhongsheng.xu@hand-china.com 2021-03-23 20:16:03
 */
@EnableSrmSourceOP
@SpringBootApplication
@ComponentScan(basePackages ={"gxbpm.service" })
public class SrmSourceOpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SrmSourceOpApplication.class, args);
    }

}
