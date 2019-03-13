package com.songzi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.songzi.service.HomePageService;
import com.songzi.service.dto.CheckItemOverviewDTO;
import com.songzi.service.dto.HomepageDTO;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import liquibase.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Created by Ke Qingyuan on 2019/3/3.
 */
@RestController
@RequestMapping("/api")
public class HomepageResource {
    private final Logger log = LoggerFactory.getLogger(HomepageResource.class);

    @Autowired
    private HomePageService homePageService;

    /**
     * 根据用户角色和部门获取用户的主页信息
     */
    @GetMapping("/homepage")
    @Timed
    @ApiOperation(value = "获取主页")
    public ResponseEntity<HomepageDTO> getUserHomepage(@RequestParam String user) {
        if (StringUtils.isNotEmpty(user)) {
            HomepageDTO homepageDTO = homePageService.getUserHomepage(user);
            return ResponseUtil.wrapOrNotFound(Optional.ofNullable(homepageDTO));
        }
        return null;
    }

    @GetMapping("/homepage/check-item-overview")
    @Timed
    @ApiOperation("根据部门获取提报信息")
    public ResponseEntity<CheckItemOverviewDTO> getCheckItemOverview(@RequestParam String login,
                                                                     @RequestParam String depId) {
        return null;
    }

}
