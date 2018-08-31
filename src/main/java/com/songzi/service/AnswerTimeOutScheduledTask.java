package com.songzi.service;

import com.alibaba.fastjson.JSONArray;
import com.google.common.reflect.TypeToken;
import com.songzi.domain.Examine;
import com.songzi.domain.Examiner;
import com.songzi.domain.Subject;
import com.songzi.domain.enumeration.ExamineStatus;
import com.songzi.repository.ExamineRepository;
import com.songzi.repository.ExaminerRepository;
import com.songzi.repository.SubjectRepository;
import com.songzi.util.SpringContextUtils;
import com.songzi.web.rest.vm.QuestionVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AnswerTimeOutScheduledTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(AnswerTimeOutScheduledTask.class);

    private SubjectRepository subjectRepository;

    private ExamineRepository examineRepository;

    private List<Examine> examineList;

    private ExaminerRepository examinerRepository;

    ExamineService examineService;

    public AnswerTimeOutScheduledTask(List<Examine> examineList, SubjectRepository subjectRepository, ExamineRepository examineRepository, ExaminerRepository examinerRepository) {
        this.examineList = examineList;
        this.subjectRepository = subjectRepository;
        this.examineRepository = examineRepository;
        this.examinerRepository = examinerRepository;
    }

    @Override
    public void run() {
        if (examineList == null) {
            examineList = new ArrayList<>();
        }
        for (Examine examine : examineList) {
            try {
                doTimeOutMethod(examine);
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
    }

    public void doTimeOutMethod(Examine examine) {

        //判断答题时间是否到了
        Instant now = Instant.now();
        Instant createdTime = examine.getCreatedDate();
        if (now.getEpochSecond() - createdTime.getEpochSecond() < examine.getDuration()) {
            //答题时间还未到  不执行以下逻辑
            LocalDateTime examineDate = LocalDateTime.ofInstant(examine.getCreatedDate(), ZoneId.of("Asia/Shanghai"));
            if (examineDate.getMonth() != LocalDateTime.now().getMonth()) {
                //答题时间没到 但是跨月份了
            } else {
                return;
            }
        }
        List<QuestionVM> questionVMList = new ArrayList<>();
        // 获取考试结果集
        String result = examine.getResult();
        if (!StringUtils.isEmpty(result))
            questionVMList = JSONArray.parseObject(result, new TypeToken<List<QuestionVM>>() {
            }.getType());

        if (examineService == null)
            examineService = SpringContextUtils.getBean(ExamineService.class);
        float score = examineService.calculateGrades(examine, questionVMList);
        examine.setScore(score);
        examine.setStatus(ExamineStatus.FINISHED);
        examine.setActualDuration(examine.getDuration());
        Long userId = examine.getUserId();
        Examiner examiner = examinerRepository.findOneByUserId(userId);
        int times = examiner.getTime();
        examiner.setTime(times + 1);
        // 保存考生
        examinerRepository.save(examiner);
        // 保存考试
        examineRepository.save(examine);
    }
}
