package com.songzi.web.rest.errors;

public class LoginAlreadyUsedException extends BadRequestAlertException {

    public LoginAlreadyUsedException() {
        super(ErrorConstants.LOGIN_ALREADY_USED_TYPE, "登陆账号已经存在", "userManagement", "userexists");
    }
}
