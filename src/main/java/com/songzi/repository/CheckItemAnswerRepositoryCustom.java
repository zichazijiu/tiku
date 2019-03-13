package com.songzi.repository;

import com.songzi.service.dto.CheckItemAnswerDTO;

import java.util.List;

/**
 * Created by Ke Qingyuan on 2019/3/13.
 */
public interface CheckItemAnswerRepositoryCustom {
    /**
     * 根据登录用户名称和部门ID
     * @param login
     * @param deptId
     * @return
     */
    List<CheckItemAnswerDTO> findAll(String login, Long deptId);
}
