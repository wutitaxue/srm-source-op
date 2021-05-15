package org.srm.source.cux.domain.entity;

import org.hzero.mybatis.domian.SecurityToken;
import org.srm.source.share.domain.entity.Clarify;

public class RcwlClarifyForBPM extends Clarify {
    private String processInstanceId;
    private String webserviceUrl;

    @Override
    public Class<? extends SecurityToken> associateEntityClass() {
        return Clarify.class;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getWebserviceUrl() {
        return webserviceUrl;
    }

    public void setWebserviceUrl(String webserviceUrl) {
        this.webserviceUrl = webserviceUrl;
    }
}
