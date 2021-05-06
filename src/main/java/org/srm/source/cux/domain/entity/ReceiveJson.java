package org.srm.source.cux.domain.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class ReceiveJson {
    /**
     * E_RESPONSE : {"MESSAGE":{"DNSTATUS":"处理成功","DOCNUM":1,"LIST":[{"UserJLName":"dhYBXuTcaok=","UserEmpStatusName":"试用员工","UserManoeuverDate":"2020-12-10 00:00:00.000","UserPositionHROrgUnitNo":"000101.17","UserSTEXT":"部门主管","Email":"zhangs13@sunac.com.cn","UserPersonRZTypeID":"","UserZSID":"RC_北京融创融科地产有限公司_一次授权","UserFXCST":"CA09200004","ReOpenid":"","UserSex":"1","UserWERKS":"1000","UserAddress":"","UserARTXT":"安宁","UserStatus":"Active","UserSTELL":"50194768","UserCreate":"2020-12-11 11:18:32.000","UserPosmemfControlUnitNo":"000101.17","UserCBCST":"CA09200004","UserCONAR":"AN","UserZYTTEXT":"地产","UserZDHRZZCXJ":"","UserLogin":"ZHANGS13","UserOrganID":"FOGCUKFHS1KuZuZ8I+gM5MznrtQ=","UserEmpType":"Full-Time","UserZLEADER":"","UserEffectiveDate":"","UserPGTXT":"","UserCountry":"中国","UserEndDate":"","UserBTRTL":"1200","UserZBCXX3":"","UserZBCXX2":"","UserZBCXX1":"","UserPersonHROrgunitNumber":"000101.17","UserZBCXX4":"","UserIDCardNo":"LwPRIxcuAk0BYwYF8LEvTdbyPFZh2Thy","UserPTEXT":"经营层","UserSLTXT":"高中","UserVIP":"0","UserJGName":"M1.1","UserEmpNo":"28042255","UserPERSG":"","UserZJSJID":"","UserEmployeeClassIfyName":"融创员工","UserPositionMemberID":"","UserCountryID":"","UserZJSJN":"","UserPersonRZTypeName":"","UserEXT1":"","UserBirthday":"1997-01-29","UserSLART":"Z8","UserEXT3":"","UserEXT2":"","UserEXT5":"","UserPERSK":"10","UserPositionHROrgUnitID":"5gsAAADkV+3M567U","UserEXT4":"","UserFolkID":"HA","UserEmpStatus":"002","UserEmployeeID":"","UserPBTXT":"融创集团","UserZYT":"0100","UserPosmemfControlUnitID":"5gsAAADkV+3M567U","UserFolkName":"汉族","UserExpiryDate":"","UserINSTI":"三中","UserPositionID":"50247841","UserEmployeeClassIfyNumber":"01","UserORGEH":"10000030","UserBTEXT":"地产/非营销","UserPassPortNo":"gilbCwunRvg=","UserEmployeeClassifyID":"5gsAAADPlrfRa+x9","UserHireDate":"2020-12-10","Mobile":"13521871670","UserOrgDisplayName":"融创中国_集团本部_人力行政中心","UserPersonRZTypeNumber":"","UserWRKPL":"","UserPersonHROrgunitName":"集团本部","UserPositionHROrgUnitName":"集团本部","UserPosmemfControlUnitName":"集团本部","Username":"张三","UserZZCXJ":"","UserUpdate":"2020-12-11 11:18:32.000","UserPersonHROrgunitID":"5gsAAADkV+3M567U","UserPLANS":"50247841","UserPositionName":"","UserBeginDate":"","UserDeptNo":"000101.11","UserPositionNumber":"50247841","UserFACH3":"无"},{"UserJLName":"rff/YNET4CI=","UserEmpStatusName":"正式员工","UserManoeuverDate":"2020-12-09 00:00:00.000","UserPositionHROrgUnitNo":"000101.17","UserSTEXT":"金牌置业顾问","Email":"c3@sunac.com.cn","UserPersonRZTypeID":"","UserZSID":"RC_北京融创融科地产有限公司_一次授权","UserFXCST":"CW10200001","ReOpenid":"","UserSex":"2","UserWERKS":"1000","UserAddress":"","UserARTXT":"不投保","UserStatus":"Active","UserSTELL":"38020477","UserCreate":"2020-12-11 10:15:59.000","UserPosmemfControlUnitNo":"000101.17","UserCBCST":"CW10200001","UserCONAR":"Z0","UserZYTTEXT":"地产","UserZDHRZZCXJ":"","UserLogin":"C3","UserOrganID":"5gsAAADkV/zM567U","UserEmpType":"Full-Time","UserZLEADER":"","UserEffectiveDate":"","UserPGTXT":"","UserCountry":"中国","UserEndDate":"","UserBTRTL":"1100","UserZBCXX3":"","UserZBCXX2":"","UserZBCXX1":"","UserPersonHROrgunitNumber":"000101.17","UserZBCXX4":"","UserIDCardNo":"3G2Oxpti5zjflfRGKyjfPJ9v9nenBpAb","UserPTEXT":"销售","UserSLTXT":"本科","UserVIP":"0","UserJGName":"X7","UserEmpNo":"28042269","UserPERSG":"","UserZJSJID":"","UserEmployeeClassIfyName":"融创员工","UserPositionMemberID":"","UserCountryID":"","UserZJSJN":"","UserPersonRZTypeName":"","UserEXT1":"","UserBirthday":"1998-01-01","UserSLART":"Z3","UserEXT3":"","UserEXT2":"","UserEXT5":"","UserPERSK":"39","UserPositionHROrgUnitID":"5gsAAADkV+3M567U","UserEXT4":"","UserFolkID":"AC","UserEmpStatus":"001","UserEmployeeID":"","UserPBTXT":"融创集团","UserZYT":"0100","UserPosmemfControlUnitID":"5gsAAADkV+3M567U","UserFolkName":"阿昌族","UserExpiryDate":"","UserINSTI":"1","UserPositionID":"50247859","UserEmployeeClassIfyNumber":"01","UserORGEH":"10000002","UserBTEXT":"地产/营销","UserPassPortNo":"gilbCwunRvg=","UserEmployeeClassifyID":"5gsAAADPlrfRa+x9","UserHireDate":"2020-12-09","Mobile":"15811573517","UserOrgDisplayName":"融创中国_集团本部_集团本部_集团本部","UserPersonRZTypeNumber":"","UserWRKPL":"","UserPersonHROrgunitName":"集团本部","UserPositionHROrgUnitName":"集团本部","UserPosmemfControlUnitName":"集团本部","Username":"CESHI6","UserZZCXJ":"","UserUpdate":"2020-12-11 10:15:59.000","UserPersonHROrgunitID":"5gsAAADkV+3M567U","UserPLANS":"50247859","UserPositionName":"","UserBeginDate":"","UserDeptNo":"000101.18","UserPositionNumber":"50247859","UserFACH3":""}],"COMPANY":"C001"},"RSP_BASEINFO":{"RSP_SRC_SYS":"QHRCLNT500","RSP_REQ_TRACEID":"8E623C46453E5CAA880467FEDDC88B54","RSP_STATUS_CODE":"S000","RSP_RETRY_TIMES":1,"RSP_TRACE_ID":"0a195a2aa9584e5684d467561253f168","RSP_BSN_ID":"8E623C46453E5CAA880467FEDDC88B54","RSP_SEND_TIME":"20210316194602155","RSP_STATUS_MSG":"处理成功","RSP_TAR_SYS":"BS-ESS-Q","RSP_SERVER_NAME":"ç\u0094µå­\u0090ç­¾ç« ç³»ç»\u009f","RESULT":0,"BIZTRANSACTIONID":"ESS_FZ_IF262_20191220090731243_8E623C46453E5CAA880467FEDDC88B54"}}
     */

    @SerializedName("E_RESPONSE")
    private ERESPONSEDTO eResponse;

    @NoArgsConstructor
    @Data
    public static class ERESPONSEDTO {
        /**
         * MESSAGE : {"DNSTATUS":"处理成功","DOCNUM":1,"LIST":[{"UserJLName":"dhYBXuTcaok=","UserEmpStatusName":"试用员工","UserManoeuverDate":"2020-12-10 00:00:00.000","UserPositionHROrgUnitNo":"000101.17","UserSTEXT":"部门主管","Email":"zhangs13@sunac.com.cn","UserPersonRZTypeID":"","UserZSID":"RC_北京融创融科地产有限公司_一次授权","UserFXCST":"CA09200004","ReOpenid":"","UserSex":"1","UserWERKS":"1000","UserAddress":"","UserARTXT":"安宁","UserStatus":"Active","UserSTELL":"50194768","UserCreate":"2020-12-11 11:18:32.000","UserPosmemfControlUnitNo":"000101.17","UserCBCST":"CA09200004","UserCONAR":"AN","UserZYTTEXT":"地产","UserZDHRZZCXJ":"","UserLogin":"ZHANGS13","UserOrganID":"FOGCUKFHS1KuZuZ8I+gM5MznrtQ=","UserEmpType":"Full-Time","UserZLEADER":"","UserEffectiveDate":"","UserPGTXT":"","UserCountry":"中国","UserEndDate":"","UserBTRTL":"1200","UserZBCXX3":"","UserZBCXX2":"","UserZBCXX1":"","UserPersonHROrgunitNumber":"000101.17","UserZBCXX4":"","UserIDCardNo":"LwPRIxcuAk0BYwYF8LEvTdbyPFZh2Thy","UserPTEXT":"经营层","UserSLTXT":"高中","UserVIP":"0","UserJGName":"M1.1","UserEmpNo":"28042255","UserPERSG":"","UserZJSJID":"","UserEmployeeClassIfyName":"融创员工","UserPositionMemberID":"","UserCountryID":"","UserZJSJN":"","UserPersonRZTypeName":"","UserEXT1":"","UserBirthday":"1997-01-29","UserSLART":"Z8","UserEXT3":"","UserEXT2":"","UserEXT5":"","UserPERSK":"10","UserPositionHROrgUnitID":"5gsAAADkV+3M567U","UserEXT4":"","UserFolkID":"HA","UserEmpStatus":"002","UserEmployeeID":"","UserPBTXT":"融创集团","UserZYT":"0100","UserPosmemfControlUnitID":"5gsAAADkV+3M567U","UserFolkName":"汉族","UserExpiryDate":"","UserINSTI":"三中","UserPositionID":"50247841","UserEmployeeClassIfyNumber":"01","UserORGEH":"10000030","UserBTEXT":"地产/非营销","UserPassPortNo":"gilbCwunRvg=","UserEmployeeClassifyID":"5gsAAADPlrfRa+x9","UserHireDate":"2020-12-10","Mobile":"13521871670","UserOrgDisplayName":"融创中国_集团本部_人力行政中心","UserPersonRZTypeNumber":"","UserWRKPL":"","UserPersonHROrgunitName":"集团本部","UserPositionHROrgUnitName":"集团本部","UserPosmemfControlUnitName":"集团本部","Username":"张三","UserZZCXJ":"","UserUpdate":"2020-12-11 11:18:32.000","UserPersonHROrgunitID":"5gsAAADkV+3M567U","UserPLANS":"50247841","UserPositionName":"","UserBeginDate":"","UserDeptNo":"000101.11","UserPositionNumber":"50247841","UserFACH3":"无"},{"UserJLName":"rff/YNET4CI=","UserEmpStatusName":"正式员工","UserManoeuverDate":"2020-12-09 00:00:00.000","UserPositionHROrgUnitNo":"000101.17","UserSTEXT":"金牌置业顾问","Email":"c3@sunac.com.cn","UserPersonRZTypeID":"","UserZSID":"RC_北京融创融科地产有限公司_一次授权","UserFXCST":"CW10200001","ReOpenid":"","UserSex":"2","UserWERKS":"1000","UserAddress":"","UserARTXT":"不投保","UserStatus":"Active","UserSTELL":"38020477","UserCreate":"2020-12-11 10:15:59.000","UserPosmemfControlUnitNo":"000101.17","UserCBCST":"CW10200001","UserCONAR":"Z0","UserZYTTEXT":"地产","UserZDHRZZCXJ":"","UserLogin":"C3","UserOrganID":"5gsAAADkV/zM567U","UserEmpType":"Full-Time","UserZLEADER":"","UserEffectiveDate":"","UserPGTXT":"","UserCountry":"中国","UserEndDate":"","UserBTRTL":"1100","UserZBCXX3":"","UserZBCXX2":"","UserZBCXX1":"","UserPersonHROrgunitNumber":"000101.17","UserZBCXX4":"","UserIDCardNo":"3G2Oxpti5zjflfRGKyjfPJ9v9nenBpAb","UserPTEXT":"销售","UserSLTXT":"本科","UserVIP":"0","UserJGName":"X7","UserEmpNo":"28042269","UserPERSG":"","UserZJSJID":"","UserEmployeeClassIfyName":"融创员工","UserPositionMemberID":"","UserCountryID":"","UserZJSJN":"","UserPersonRZTypeName":"","UserEXT1":"","UserBirthday":"1998-01-01","UserSLART":"Z3","UserEXT3":"","UserEXT2":"","UserEXT5":"","UserPERSK":"39","UserPositionHROrgUnitID":"5gsAAADkV+3M567U","UserEXT4":"","UserFolkID":"AC","UserEmpStatus":"001","UserEmployeeID":"","UserPBTXT":"融创集团","UserZYT":"0100","UserPosmemfControlUnitID":"5gsAAADkV+3M567U","UserFolkName":"阿昌族","UserExpiryDate":"","UserINSTI":"1","UserPositionID":"50247859","UserEmployeeClassIfyNumber":"01","UserORGEH":"10000002","UserBTEXT":"地产/营销","UserPassPortNo":"gilbCwunRvg=","UserEmployeeClassifyID":"5gsAAADPlrfRa+x9","UserHireDate":"2020-12-09","Mobile":"15811573517","UserOrgDisplayName":"融创中国_集团本部_集团本部_集团本部","UserPersonRZTypeNumber":"","UserWRKPL":"","UserPersonHROrgunitName":"集团本部","UserPositionHROrgUnitName":"集团本部","UserPosmemfControlUnitName":"集团本部","Username":"CESHI6","UserZZCXJ":"","UserUpdate":"2020-12-11 10:15:59.000","UserPersonHROrgunitID":"5gsAAADkV+3M567U","UserPLANS":"50247859","UserPositionName":"","UserBeginDate":"","UserDeptNo":"000101.18","UserPositionNumber":"50247859","UserFACH3":""}],"COMPANY":"C001"}
         * RSP_BASEINFO : {"RSP_SRC_SYS":"QHRCLNT500","RSP_REQ_TRACEID":"8E623C46453E5CAA880467FEDDC88B54","RSP_STATUS_CODE":"S000","RSP_RETRY_TIMES":1,"RSP_TRACE_ID":"0a195a2aa9584e5684d467561253f168","RSP_BSN_ID":"8E623C46453E5CAA880467FEDDC88B54","RSP_SEND_TIME":"20210316194602155","RSP_STATUS_MSG":"处理成功","RSP_TAR_SYS":"BS-ESS-Q","RSP_SERVER_NAME":"ç\u0094µå­\u0090ç­¾ç« ç³»ç»\u009f","RESULT":0,"BIZTRANSACTIONID":"ESS_FZ_IF262_20191220090731243_8E623C46453E5CAA880467FEDDC88B54"}
         */

        @SerializedName("MESSAGE")
        private MESSAGEDTO message;
        @SerializedName("RSP_BASEINFO")
        private RSPBASEINFODTO rspBaseinfo;

        @NoArgsConstructor
        @Data
        public static class MESSAGEDTO {
            /**
             * DNSTATUS : 处理成功
             * DOCNUM : 1
             * LIST : [{"UserJLName":"dhYBXuTcaok=","UserEmpStatusName":"试用员工","UserManoeuverDate":"2020-12-10 00:00:00.000","UserPositionHROrgUnitNo":"000101.17","UserSTEXT":"部门主管","Email":"zhangs13@sunac.com.cn","UserPersonRZTypeID":"","UserZSID":"RC_北京融创融科地产有限公司_一次授权","UserFXCST":"CA09200004","ReOpenid":"","UserSex":"1","UserWERKS":"1000","UserAddress":"","UserARTXT":"安宁","UserStatus":"Active","UserSTELL":"50194768","UserCreate":"2020-12-11 11:18:32.000","UserPosmemfControlUnitNo":"000101.17","UserCBCST":"CA09200004","UserCONAR":"AN","UserZYTTEXT":"地产","UserZDHRZZCXJ":"","UserLogin":"ZHANGS13","UserOrganID":"FOGCUKFHS1KuZuZ8I+gM5MznrtQ=","UserEmpType":"Full-Time","UserZLEADER":"","UserEffectiveDate":"","UserPGTXT":"","UserCountry":"中国","UserEndDate":"","UserBTRTL":"1200","UserZBCXX3":"","UserZBCXX2":"","UserZBCXX1":"","UserPersonHROrgunitNumber":"000101.17","UserZBCXX4":"","UserIDCardNo":"LwPRIxcuAk0BYwYF8LEvTdbyPFZh2Thy","UserPTEXT":"经营层","UserSLTXT":"高中","UserVIP":"0","UserJGName":"M1.1","UserEmpNo":"28042255","UserPERSG":"","UserZJSJID":"","UserEmployeeClassIfyName":"融创员工","UserPositionMemberID":"","UserCountryID":"","UserZJSJN":"","UserPersonRZTypeName":"","UserEXT1":"","UserBirthday":"1997-01-29","UserSLART":"Z8","UserEXT3":"","UserEXT2":"","UserEXT5":"","UserPERSK":"10","UserPositionHROrgUnitID":"5gsAAADkV+3M567U","UserEXT4":"","UserFolkID":"HA","UserEmpStatus":"002","UserEmployeeID":"","UserPBTXT":"融创集团","UserZYT":"0100","UserPosmemfControlUnitID":"5gsAAADkV+3M567U","UserFolkName":"汉族","UserExpiryDate":"","UserINSTI":"三中","UserPositionID":"50247841","UserEmployeeClassIfyNumber":"01","UserORGEH":"10000030","UserBTEXT":"地产/非营销","UserPassPortNo":"gilbCwunRvg=","UserEmployeeClassifyID":"5gsAAADPlrfRa+x9","UserHireDate":"2020-12-10","Mobile":"13521871670","UserOrgDisplayName":"融创中国_集团本部_人力行政中心","UserPersonRZTypeNumber":"","UserWRKPL":"","UserPersonHROrgunitName":"集团本部","UserPositionHROrgUnitName":"集团本部","UserPosmemfControlUnitName":"集团本部","Username":"张三","UserZZCXJ":"","UserUpdate":"2020-12-11 11:18:32.000","UserPersonHROrgunitID":"5gsAAADkV+3M567U","UserPLANS":"50247841","UserPositionName":"","UserBeginDate":"","UserDeptNo":"000101.11","UserPositionNumber":"50247841","UserFACH3":"无"},{"UserJLName":"rff/YNET4CI=","UserEmpStatusName":"正式员工","UserManoeuverDate":"2020-12-09 00:00:00.000","UserPositionHROrgUnitNo":"000101.17","UserSTEXT":"金牌置业顾问","Email":"c3@sunac.com.cn","UserPersonRZTypeID":"","UserZSID":"RC_北京融创融科地产有限公司_一次授权","UserFXCST":"CW10200001","ReOpenid":"","UserSex":"2","UserWERKS":"1000","UserAddress":"","UserARTXT":"不投保","UserStatus":"Active","UserSTELL":"38020477","UserCreate":"2020-12-11 10:15:59.000","UserPosmemfControlUnitNo":"000101.17","UserCBCST":"CW10200001","UserCONAR":"Z0","UserZYTTEXT":"地产","UserZDHRZZCXJ":"","UserLogin":"C3","UserOrganID":"5gsAAADkV/zM567U","UserEmpType":"Full-Time","UserZLEADER":"","UserEffectiveDate":"","UserPGTXT":"","UserCountry":"中国","UserEndDate":"","UserBTRTL":"1100","UserZBCXX3":"","UserZBCXX2":"","UserZBCXX1":"","UserPersonHROrgunitNumber":"000101.17","UserZBCXX4":"","UserIDCardNo":"3G2Oxpti5zjflfRGKyjfPJ9v9nenBpAb","UserPTEXT":"销售","UserSLTXT":"本科","UserVIP":"0","UserJGName":"X7","UserEmpNo":"28042269","UserPERSG":"","UserZJSJID":"","UserEmployeeClassIfyName":"融创员工","UserPositionMemberID":"","UserCountryID":"","UserZJSJN":"","UserPersonRZTypeName":"","UserEXT1":"","UserBirthday":"1998-01-01","UserSLART":"Z3","UserEXT3":"","UserEXT2":"","UserEXT5":"","UserPERSK":"39","UserPositionHROrgUnitID":"5gsAAADkV+3M567U","UserEXT4":"","UserFolkID":"AC","UserEmpStatus":"001","UserEmployeeID":"","UserPBTXT":"融创集团","UserZYT":"0100","UserPosmemfControlUnitID":"5gsAAADkV+3M567U","UserFolkName":"阿昌族","UserExpiryDate":"","UserINSTI":"1","UserPositionID":"50247859","UserEmployeeClassIfyNumber":"01","UserORGEH":"10000002","UserBTEXT":"地产/营销","UserPassPortNo":"gilbCwunRvg=","UserEmployeeClassifyID":"5gsAAADPlrfRa+x9","UserHireDate":"2020-12-09","Mobile":"15811573517","UserOrgDisplayName":"融创中国_集团本部_集团本部_集团本部","UserPersonRZTypeNumber":"","UserWRKPL":"","UserPersonHROrgunitName":"集团本部","UserPositionHROrgUnitName":"集团本部","UserPosmemfControlUnitName":"集团本部","Username":"CESHI6","UserZZCXJ":"","UserUpdate":"2020-12-11 10:15:59.000","UserPersonHROrgunitID":"5gsAAADkV+3M567U","UserPLANS":"50247859","UserPositionName":"","UserBeginDate":"","UserDeptNo":"000101.18","UserPositionNumber":"50247859","UserFACH3":""}]
             * COMPANY : C001
             */

            @SerializedName("DNSTATUS")
            private String dnstatus;
            @SerializedName("DOCNUM")
            private Integer docnum;
            @SerializedName("LIST")
            private List<LISTDTO> list;
            @SerializedName("COMPANY")
            private String company;

            @NoArgsConstructor
            @Data
            public static class LISTDTO {
                /**
                 * UserJLName : dhYBXuTcaok=
                 * UserEmpStatusName : 试用员工
                 * UserManoeuverDate : 2020-12-10 00:00:00.000
                 * UserPositionHROrgUnitNo : 000101.17
                 * UserSTEXT : 部门主管
                 * Email : zhangs13@sunac.com.cn
                 * UserPersonRZTypeID :
                 * UserZSID : RC_北京融创融科地产有限公司_一次授权
                 * UserFXCST : CA09200004
                 * ReOpenid :
                 * UserSex : 1
                 * UserWERKS : 1000
                 * UserAddress :
                 * UserARTXT : 安宁
                 * UserStatus : Active
                 * UserSTELL : 50194768
                 * UserCreate : 2020-12-11 11:18:32.000
                 * UserPosmemfControlUnitNo : 000101.17
                 * UserCBCST : CA09200004
                 * UserCONAR : AN
                 * UserZYTTEXT : 地产
                 * UserZDHRZZCXJ :
                 * UserLogin : ZHANGS13
                 * UserOrganID : FOGCUKFHS1KuZuZ8I+gM5MznrtQ=
                 * UserEmpType : Full-Time
                 * UserZLEADER :
                 * UserEffectiveDate :
                 * UserPGTXT :
                 * UserCountry : 中国
                 * UserEndDate :
                 * UserBTRTL : 1200
                 * UserZBCXX3 :
                 * UserZBCXX2 :
                 * UserZBCXX1 :
                 * UserPersonHROrgunitNumber : 000101.17
                 * UserZBCXX4 :
                 * UserIDCardNo : LwPRIxcuAk0BYwYF8LEvTdbyPFZh2Thy
                 * UserPTEXT : 经营层
                 * UserSLTXT : 高中
                 * UserVIP : 0
                 * UserJGName : M1.1
                 * UserEmpNo : 28042255
                 * UserPERSG :
                 * UserZJSJID :
                 * UserEmployeeClassIfyName : 融创员工
                 * UserPositionMemberID :
                 * UserCountryID :
                 * UserZJSJN :
                 * UserPersonRZTypeName :
                 * UserEXT1 :
                 * UserBirthday : 1997-01-29
                 * UserSLART : Z8
                 * UserEXT3 :
                 * UserEXT2 :
                 * UserEXT5 :
                 * UserPERSK : 10
                 * UserPositionHROrgUnitID : 5gsAAADkV+3M567U
                 * UserEXT4 :
                 * UserFolkID : HA
                 * UserEmpStatus : 002
                 * UserEmployeeID :
                 * UserPBTXT : 融创集团
                 * UserZYT : 0100
                 * UserPosmemfControlUnitID : 5gsAAADkV+3M567U
                 * UserFolkName : 汉族
                 * UserExpiryDate :
                 * UserINSTI : 三中
                 * UserPositionID : 50247841
                 * UserEmployeeClassIfyNumber : 01
                 * UserORGEH : 10000030
                 * UserBTEXT : 地产/非营销
                 * UserPassPortNo : gilbCwunRvg=
                 * UserEmployeeClassifyID : 5gsAAADPlrfRa+x9
                 * UserHireDate : 2020-12-10
                 * Mobile : 13521871670
                 * UserOrgDisplayName : 融创中国_集团本部_人力行政中心
                 * UserPersonRZTypeNumber :
                 * UserWRKPL :
                 * UserPersonHROrgunitName : 集团本部
                 * UserPositionHROrgUnitName : 集团本部
                 * UserPosmemfControlUnitName : 集团本部
                 * Username : 张三
                 * UserZZCXJ :
                 * UserUpdate : 2020-12-11 11:18:32.000
                 * UserPersonHROrgunitID : 5gsAAADkV+3M567U
                 * UserPLANS : 50247841
                 * UserPositionName :
                 * UserBeginDate :
                 * UserDeptNo : 000101.11
                 * UserPositionNumber : 50247841
                 * UserFACH3 : 无
                 */

                @SerializedName("UserJLName")
                private String UserJLName;
                @SerializedName("UserEmpStatusName")
                private String UserEmpStatusName;
                @SerializedName("UserManoeuverDate")
                private String UserManoeuverDate;
                @SerializedName("UserPositionHROrgUnitNo")
                private String UserPositionHROrgUnitNo;
                @SerializedName("UserSTEXT")
                private String UserSTEXT;
                @SerializedName("Email")
                private String Email;
                @SerializedName("UserPersonRZTypeID")
                private String UserPersonRZTypeID;
                @SerializedName("UserZSID")
                private String UserZSID;
                @SerializedName("UserFXCST")
                private String UserFXCST;
                @SerializedName("ReOpenid")
                private String ReOpenid;
                @SerializedName("UserSex")
                private String UserSex;
                @SerializedName("UserWERKS")
                private String UserWERKS;
                @SerializedName("UserAddress")
                private String UserAddress;
                @SerializedName("UserARTXT")
                private String UserARTXT;
                @SerializedName("UserStatus")
                private String UserStatus;
                @SerializedName("UserSTELL")
                private String UserSTELL;
                @SerializedName("UserCreate")
                private String UserCreate;
                @SerializedName("UserPosmemfControlUnitNo")
                private String UserPosmemfControlUnitNo;
                @SerializedName("UserCBCST")
                private String UserCBCST;
                @SerializedName("UserCONAR")
                private String UserCONAR;
                @SerializedName("UserZYTTEXT")
                private String UserZYTTEXT;
                @SerializedName("UserZDHRZZCXJ")
                private String UserZDHRZZCXJ;
                @SerializedName("UserLogin")
                private String UserLogin;
                @SerializedName("UserOrganID")
                private String UserOrganID;
                @SerializedName("UserEmpType")
                private String UserEmpType;
                @SerializedName("UserZLEADER")
                private String UserZLEADER;
                @SerializedName("UserEffectiveDate")
                private String UserEffectiveDate;
                @SerializedName("UserPGTXT")
                private String UserPGTXT;
                @SerializedName("UserCountry")
                private String UserCountry;
                @SerializedName("UserEndDate")
                private String UserEndDate;
                @SerializedName("UserBTRTL")
                private String UserBTRTL;
                @SerializedName("UserZBCXX3")
                private String UserZBCXX3;
                @SerializedName("UserZBCXX2")
                private String UserZBCXX2;
                @SerializedName("UserZBCXX1")
                private String UserZBCXX1;
                @SerializedName("UserPersonHROrgunitNumber")
                private String UserPersonHROrgunitNumber;
                @SerializedName("UserZBCXX4")
                private String UserZBCXX4;
                @SerializedName("UserIDCardNo")
                private String UserIDCardNo;
                @SerializedName("UserPTEXT")
                private String UserPTEXT;
                @SerializedName("UserSLTXT")
                private String UserSLTXT;
                @SerializedName("UserVIP")
                private String UserVIP;
                @SerializedName("UserJGName")
                private String UserJGName;
                @SerializedName("UserEmpNo")
                private String UserEmpNo;
                @SerializedName("UserPERSG")
                private String UserPERSG;
                @SerializedName("UserZJSJID")
                private String UserZJSJID;
                @SerializedName("UserEmployeeClassIfyName")
                private String UserEmployeeClassIfyName;
                @SerializedName("UserPositionMemberID")
                private String UserPositionMemberID;
                @SerializedName("UserCountryID")
                private String UserCountryID;
                @SerializedName("UserZJSJN")
                private String UserZJSJN;
                @SerializedName("UserPersonRZTypeName")
                private String UserPersonRZTypeName;
                @SerializedName("UserEXT1")
                private String UserEXT1;
                @SerializedName("UserBirthday")
                private String UserBirthday;
                @SerializedName("UserSLART")
                private String UserSLART;
                @SerializedName("UserEXT3")
                private String UserEXT3;
                @SerializedName("UserEXT2")
                private String UserEXT2;
                @SerializedName("UserEXT5")
                private String UserEXT5;
                @SerializedName("UserPERSK")
                private String UserPERSK;
                @SerializedName("UserPositionHROrgUnitID")
                private String UserPositionHROrgUnitID;
                @SerializedName("UserEXT4")
                private String UserEXT4;
                @SerializedName("UserFolkID")
                private String UserFolkID;
                @SerializedName("UserEmpStatus")
                private String UserEmpStatus;
                @SerializedName("UserEmployeeID")
                private String UserEmployeeID;
                @SerializedName("UserPBTXT")
                private String UserPBTXT;
                @SerializedName("UserZYT")
                private String UserZYT;
                @SerializedName("UserPosmemfControlUnitID")
                private String UserPosmemfControlUnitID;
                @SerializedName("UserFolkName")
                private String UserFolkName;
                @SerializedName("UserExpiryDate")
                private String UserExpiryDate;
                @SerializedName("UserINSTI")
                private String UserINSTI;
                @SerializedName("UserPositionID")
                private String UserPositionID;
                @SerializedName("UserEmployeeClassIfyNumber")
                private String UserEmployeeClassIfyNumber;
                @SerializedName("UserORGEH")
                private String UserORGEH;
                @SerializedName("UserBTEXT")
                private String UserBTEXT;
                @SerializedName("UserPassPortNo")
                private String UserPassPortNo;
                @SerializedName("UserEmployeeClassifyID")
                private String UserEmployeeClassifyID;
                @SerializedName("UserHireDate")
                private String UserHireDate;
                @SerializedName("Mobile")
                private String Mobile;
                @SerializedName("UserOrgDisplayName")
                private String UserOrgDisplayName;
                @SerializedName("UserPersonRZTypeNumber")
                private String UserPersonRZTypeNumber;
                @SerializedName("UserWRKPL")
                private String UserWRKPL;
                @SerializedName("UserPersonHROrgunitName")
                private String UserPersonHROrgunitName;
                @SerializedName("UserPositionHROrgUnitName")
                private String UserPositionHROrgUnitName;
                @SerializedName("UserPosmemfControlUnitName")
                private String UserPosmemfControlUnitName;
                @SerializedName("Username")
                private String Username;
                @SerializedName("UserZZCXJ")
                private String UserZZCXJ;
                @SerializedName("UserUpdate")
                private String UserUpdate;
                @SerializedName("UserPersonHROrgunitID")
                private String UserPersonHROrgunitID;
                @SerializedName("UserPLANS")
                private String UserPLANS;
                @SerializedName("UserPositionName")
                private String UserPositionName;
                @SerializedName("UserBeginDate")
                private String UserBeginDate;
                @SerializedName("UserDeptNo")
                private String UserDeptNo;
                @SerializedName("UserPositionNumber")
                private String UserPositionNumber;
                @SerializedName("UserFACH3")
                private String UserFACH3;
            }
        }

        @NoArgsConstructor
        @Data
        public static class RSPBASEINFODTO {
            /**
             * RSP_SRC_SYS : QHRCLNT500
             * RSP_REQ_TRACEID : 8E623C46453E5CAA880467FEDDC88B54
             * RSP_STATUS_CODE : S000
             * RSP_RETRY_TIMES : 1
             * RSP_TRACE_ID : 0a195a2aa9584e5684d467561253f168
             * RSP_BSN_ID : 8E623C46453E5CAA880467FEDDC88B54
             * RSP_SEND_TIME : 20210316194602155
             * RSP_STATUS_MSG : 处理成功
             * RSP_TAR_SYS : BS-ESS-Q
             * RSP_SERVER_NAME : çµå­ç­¾ç« ç³»ç»
             * RESULT : 0
             * BIZTRANSACTIONID : ESS_FZ_IF262_20191220090731243_8E623C46453E5CAA880467FEDDC88B54
             */

            @SerializedName("RSP_SRC_SYS")
            private String rspSrcSys;
            @SerializedName("RSP_REQ_TRACEID")
            private String rspReqTraceid;
            @SerializedName("RSP_STATUS_CODE")
            private String rspStatusCode;
            @SerializedName("RSP_RETRY_TIMES")
            private Integer rspRetryTimes;
            @SerializedName("RSP_TRACE_ID")
            private String rspTraceId;
            @SerializedName("RSP_BSN_ID")
            private String rspBsnId;
            @SerializedName("RSP_SEND_TIME")
            private String rspSendTime;
            @SerializedName("RSP_STATUS_MSG")
            private String rspStatusMsg;
            @SerializedName("RSP_TAR_SYS")
            private String rspTarSys;
            @SerializedName("RSP_SERVER_NAME")
            private String rspServerName;
            @SerializedName("RESULT")
            private Integer result;
            @SerializedName("BIZTRANSACTIONID")
            private String biztransactionid;
        }
    }

    public ERESPONSEDTO geteResponse() {
        return eResponse;
    }

    public void seteResponse(ERESPONSEDTO eResponse) {
        this.eResponse = eResponse;
    }
}
