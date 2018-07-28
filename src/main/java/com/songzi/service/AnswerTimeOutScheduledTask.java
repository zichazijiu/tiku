package com.songzi.service;

import com.alibaba.fastjson.JSONArray;
import com.google.common.reflect.TypeToken;
import com.songzi.domain.Examine;
import com.songzi.domain.Subject;
import com.songzi.domain.enumeration.ExamineStatus;
import com.songzi.repository.ExamineRepository;
import com.songzi.repository.SubjectRepository;
import com.songzi.web.rest.vm.QuestionVM;
import org.springframework.beans.factory.annotation.Autowired;

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

    private SubjectRepository subjectRepository;

    private ExamineRepository examineRepository;

    private List<Examine> examineList;

    @Autowired
    private ExamineSubjectService examineSubjectService;

    public AnswerTimeOutScheduledTask(List<Examine> examineList, SubjectRepository subjectRepository, ExamineRepository examineRepository){
        this.examineList = examineList;
        this.subjectRepository = subjectRepository;
        this.examineRepository = examineRepository;
    }

    @Override
    public void run() {
        if(examineList == null){
            examineList = new ArrayList<>();
        }
        for(Examine examine : examineList){
            try{
                doTimeOutMethod(examine);
            }catch (Exception e){

            }
        }
    }

    public void doTimeOutMethod(Examine examine){

        //判断答题时间是否到了
        Instant now = Instant.now();
        Instant createdTime = examine.getCreatedDate();
        if(now.getEpochSecond() - createdTime.getEpochSecond() < examine.getDuration()){
            //答题时间还未到  不执行以下逻辑
            LocalDateTime examineDate = LocalDateTime.ofInstant(examine.getCreatedDate(),ZoneId.of("Asia/Shanghai"));
            if(examineDate.getMonth() != LocalDateTime.now().getMonth()){
                //答题时间没到 但是跨月份了
            }else{
                return;
            }
        }
        String result = examine.getResult();
        List<QuestionVM> questionVMList;

        if(result == null || "".equals(result)){
            questionVMList = new ArrayList<>();
        }else{
            questionVMList = JSONArray.parseObject(result,new TypeToken<List<QuestionVM>>() {}.getType());
        }

        Long projectId = examine.getProjectId();
        Map<Long,Subject> subjectMap = subjectRepository.findAllByProjectId(projectId).stream().collect(Collectors.toMap(Subject ::getId,Function.identity()));
        int rightCount = 0;
        int total = subjectMap.size();
        for(QuestionVM questionVM : questionVMList){
            Subject subject = subjectMap.get(questionVM.getSubjectId());
            if(subject.getRight() == questionVM.getRight()){
                examineSubjectService.doExamineSubjectCount(subject.getId());
                rightCount++;
            }
        }
        int score = rightCount* 100/total;
        examine.setScore(score);
        examine.setStatus(ExamineStatus.FINISHED);
        examine.setActualDuration(examine.getDuration());
        examine = examineRepository.save(examine);
    }
}
