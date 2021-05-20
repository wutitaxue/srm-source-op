package org.srm.source.cux.infra.constant;

import java.util.Arrays;
import java.util.List;

/**
 * @author chengzhi.guo@hand-china.com
 * @description Share 服务常量
 * @date 2019/4/16 16:25
 */
public class RcwlShareConstants {


    private RcwlShareConstants() {
    }


    /**
     * 寻源模板 寻源类别
     * RFQ 询价
     * RFA 竞价
     * BID 招投标
     */
    public static class CategoryType {
        private CategoryType() {
        }

        public static final String RFQ = "RFQ";
        public static final String RCBJ = "RCBJ";
        public static final String RCZB = "RCZB";
        public static final String RCZW = "RCZW";
        public static final String RFA = "RFA";
        public static final String BID = "BID";
        public static final String RFX = "RFX";
    }

}
