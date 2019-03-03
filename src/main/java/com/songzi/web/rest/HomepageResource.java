package com.songzi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.songzi.service.dto.HomepageDTO;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by Ke Qingyuan on 2019/3/3.
 */
@RestController
@RequestMapping("/api")
public class HomepageResource {
    private final Logger log = LoggerFactory.getLogger(HomepageResource.class);

    /**
     * 根据用户角色和部门获取用户的主页信息
     */
    @GetMapping("/homepage/{userId}")
    @Timed
    @ApiOperation(value = "获取主页")
    public ResponseEntity<HomepageDTO> getUserHomepage(@PathVariable Long userId) {
        return null;
    }
}
