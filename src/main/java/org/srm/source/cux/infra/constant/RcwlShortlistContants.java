package org.srm.source.cux.infra.constant;

/**
 * @author guotao.yu@hand-china.com 2021/01/12 11:07 上午
 */
public class RcwlShortlistContants {
    /**
     * 租户
     */
    public static final String TENANT_NUMBER = "SRM-RCWL";


    public interface CodeRule {
        //入围单编码
        String SCUX_RCWL_SHORT_HEADER_NUM = "SSRC.RCWL.SHORT_HEADER_NUM";

    }

    public interface LovCode {

        /**
         * 寻源类别
         */
        String SCUX_RCWL_SOURCE_CATEGORY = "SCUX_RCWL_SOURCE_CATEGORY";
        //直委
        String SOURCE_CATEGORY_DIRECT = "Direct";
        //比价
        String SOURCE_CATEGORY_COMPARISON ="Comparison";
        //竞价
        String SOURCE_CATEGORY_BIDDING ="Bidding";
        //招投标
        String SOURCE_CATEGORY_ATTRACT ="Attract";

        /**
         * 入围方式
         */
        String SCUX_RCWL_SHORTLIST_CATEGEORY = "SCUX_RCWL_SHORTLIST_CATEGEORY";
        //邀请
        String SHORTLIST_CATEGEORY_INVITATION = "Invitation";
        //公开征集
        String SHORTLIST_CATEGEORY_SOLICITATION = "Solicitation";

        /**
         * 入围单状态
         */
        String SSRC_RCWL_RW_STUTAS = "SSRC.RCWL.RW.STUTAS";
        //新建 单据新建的状态
        String RW_STUTAS_NEW = "new";
        //报名中 公开征集的单据发布以后，开始时间到了的状态
        String RW_STUTAS_ENROLL = "enroll";
        //报名完成 公开征集的单据发布以后，结束时间到了的状态
        String RW_STUTAS_ENROLLED = "enrolled";
        //已发布 公开征集的单据发布以后，点击发布以后开始时间未到
        String RW_STUTAS_PUBLISHED = "published";
        //审批中 报名完成的单据点击提交，bpm返回发送成功后的状态
        String RW_STUTAS_APPROVING = "approving";
        //审批拒绝 bpm返回审批拒绝
        String RW_STUTAS_REJECTED = "rejected";
        //审批通过 bpm返回审批通过的状态
        String RW_STUTAS_APPROVED = "approved";
        //删除
        String RW_STUTAS_DELETE = "delete";

        /**
         * 入围单供应商状态
         */
        String SSRC_RCWL_RWENROLL_STUTAS = "SSRC.RCWL.RWENROLL.STUTAS";
        //未开始
        String RCWL_RWENROLL_STUTAS_UNSTART = "UNSTART";
        //未报名
        String RCWL_RWENROLL_STUTAS_UNPARTICIPATED = "UNPARTICIPATED";
        //已报名
        String RCWL_RWENROLL_STUTAS_PARTICIPATED = "PARTICIPATED";
        //已放弃
        String RCWL_RWENROLL_STUTAS_ABANDONED = "ABANDONED";
        //未提交
        String RCWL_RWENROLL_STUTAS_NOTSUBMITTED = "NOTSUBMITTED";
        //已提交
        String RCWL_RWENROLL_STUTAS_SUBMITTED = "SUBMITTED";
        //未入围
        String RCWL_RWENROLL_STUTAS_PREREFUSED = "PREREFUSED";
        //已入围
        String RCWL_RWENROLL_STUTAS_SHORTLISTED = "SHORTLISTED";
        //已取消
        String RCWL_RWENROLL_STUTAS_CANCELED = "CANCELED";

    }
}
