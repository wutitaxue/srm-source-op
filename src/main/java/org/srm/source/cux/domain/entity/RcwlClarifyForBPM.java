package org.srm.source.cux.domain.entity;

import io.swagger.annotations.ApiModelProperty;
import org.hzero.mybatis.domian.SecurityToken;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.srm.source.share.domain.entity.Clarify;
import org.srm.source.share.domain.entity.IssueLine;

import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

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
