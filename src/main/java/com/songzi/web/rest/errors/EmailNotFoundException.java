package com.songzi.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class EmailNotFoundException extends AbstractThrowableProblem {

    public EmailNotFoundException() {
        super(ErrorConstants.EMAIL_NOT_FOUND_TYPE, "邮件地址没有注册", Status.BAD_REQUEST);
    }
}
