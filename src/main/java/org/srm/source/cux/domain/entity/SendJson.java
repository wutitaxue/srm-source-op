package org.srm.source.cux.domain.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SendJson {

    /**
     * I_REQUEST : {"REQ_BASEINFO":{"REQ_SRC_SYS":"BS-ESS-Q","REQ_BSN_ID":"8E623C46453E5CAA880467FEDDC88B54","REQ_REPEAT_CYCLE":"60","REQ_RETRY_TIMES":"1","REQ_REPEAT_FLAG":"1","REQ_SEND_TIME":"20191220090731243","REQ_SYN_FLAG":"0","COUNT":"1","REQ_TAR_SYS":"QHRCLNT500","REQ_SERVER_NAME":"电子签章系统","REQ_TRACE_ID":"8E623C46453E5CAA880467FEDDC88B54","BIZTRANSACTIONID":"ESS_FZ_IF262_20191220090731243_8E623C46453E5CAA880467FEDDC88B54"},"MESSAGE":{"REQ_ITEM":{"beginDate":"2020-12-09 00:08:11.000","endDate":"2020-12-11 16:08:11.000","pageNo":1,"pageRowNo":2,"systemID":"Sunac_IDMUser","account":"idmadmin","password":"idmpass"}}}
     */

    @SerializedName("I_REQUEST")
    private IREQUESTDTO iRequest;

    @NoArgsConstructor
    @Data
    public static class IREQUESTDTO {
        /**
         * REQ_BASEINFO : {"REQ_SRC_SYS":"BS-ESS-Q","REQ_BSN_ID":"8E623C46453E5CAA880467FEDDC88B54","REQ_REPEAT_CYCLE":"60","REQ_RETRY_TIMES":"1","REQ_REPEAT_FLAG":"1","REQ_SEND_TIME":"20191220090731243","REQ_SYN_FLAG":"0","COUNT":"1","REQ_TAR_SYS":"QHRCLNT500","REQ_SERVER_NAME":"电子签章系统","REQ_TRACE_ID":"8E623C46453E5CAA880467FEDDC88B54","BIZTRANSACTIONID":"ESS_FZ_IF262_20191220090731243_8E623C46453E5CAA880467FEDDC88B54"}
         * MESSAGE : {"REQ_ITEM":{"beginDate":"2020-12-09 00:08:11.000","endDate":"2020-12-11 16:08:11.000","pageNo":1,"pageRowNo":2,"systemID":"Sunac_IDMUser","account":"idmadmin","password":"idmpass"}}
         */

        @SerializedName("REQ_BASEINFO")
        private REQBASEINFODTO reqBaseinfo;
        @SerializedName("MESSAGE")
        private MESSAGEDTO message;

        @NoArgsConstructor
        @Data
        public static class REQBASEINFODTO {
            /**
             * REQ_SRC_SYS : BS-ESS-Q
             * REQ_BSN_ID : 8E623C46453E5CAA880467FEDDC88B54
             * REQ_REPEAT_CYCLE : 60
             * REQ_RETRY_TIMES : 1
             * REQ_REPEAT_FLAG : 1
             * REQ_SEND_TIME : 20191220090731243
             * REQ_SYN_FLAG : 0
             * COUNT : 1
             * REQ_TAR_SYS : QHRCLNT500
             * REQ_SERVER_NAME : 电子签章系统
             * REQ_TRACE_ID : 8E623C46453E5CAA880467FEDDC88B54
             * BIZTRANSACTIONID : ESS_FZ_IF262_20191220090731243_8E623C46453E5CAA880467FEDDC88B54
             */

            @SerializedName("REQ_SRC_SYS")
            private String reqSrcSys;
            @SerializedName("REQ_BSN_ID")
            private String reqBsnId;
            @SerializedName("REQ_REPEAT_CYCLE")
            private String reqRepeatCycle;
            @SerializedName("REQ_RETRY_TIMES")
            private String reqRetryTimes;
            @SerializedName("REQ_REPEAT_FLAG")
            private String reqRepeatFlag;
            @SerializedName("REQ_SEND_TIME")
            private String reqSendTime;
            @SerializedName("REQ_SYN_FLAG")
            private String reqSynFlag;
            @SerializedName("COUNT")
            private String count;
            @SerializedName("REQ_TAR_SYS")
            private String reqTarSys;
            @SerializedName("REQ_SERVER_NAME")
            private String reqServerName;
            @SerializedName("REQ_TRACE_ID")
            private String reqTraceId;
            @SerializedName("BIZTRANSACTIONID")
            private String biztransactionid;
        }

        @NoArgsConstructor
        @Data
        public static class MESSAGEDTO {
            /**
             * REQ_ITEM : {"beginDate":"2020-12-09 00:08:11.000","endDate":"2020-12-11 16:08:11.000","pageNo":1,"pageRowNo":2,"systemID":"Sunac_IDMUser","account":"idmadmin","password":"idmpass"}
             */

            @SerializedName("REQ_ITEM")
            private REQITEMDTO reqItem;

            @NoArgsConstructor
            @Data
            public static class REQITEMDTO {
                /**
                 * beginDate : 2020-12-09 00:08:11.000
                 * endDate : 2020-12-11 16:08:11.000
                 * pageNo : 1
                 * pageRowNo : 2
                 * systemID : Sunac_IDMUser
                 * account : idmadmin
                 * password : idmpass
                 */

                @SerializedName("beginDate")
                private String beginDate;
                @SerializedName("endDate")
                private String endDate;
                @SerializedName("pageNo")
                private Integer pageNo;
                @SerializedName("pageRowNo")
                private Integer pageRowNo;
                @SerializedName("systemID")
                private String systemID;
                @SerializedName("account")
                private String account;
                @SerializedName("password")
                private String password;
            }
        }
    }

    public IREQUESTDTO getiRequest() {
        return iRequest;
    }

    public void setiRequest(IREQUESTDTO iRequest) {
        this.iRequest = iRequest;
    }
}
