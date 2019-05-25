package com.songzi.web.rest.errors;

public class EmailAlreadyUsedException extends BadRequestAlertException {

    public EmailAlreadyUsedException() {
        super(ErrorConstants.EMAIL_ALREADY_USED_TYPE, "邮件地址已经存在", "userManagement", "emailexists");
    }
}
