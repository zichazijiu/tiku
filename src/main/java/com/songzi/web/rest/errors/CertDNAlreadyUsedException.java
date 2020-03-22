package com.songzi.web.rest.errors;

public class CertDNAlreadyUsedException extends BadRequestAlertException {

    public CertDNAlreadyUsedException() {
        super(ErrorConstants.EMAIL_ALREADY_USED_TYPE, "证书编号已经存在", "userManagement", "certDNexists");
    }
}
