package com.songzi.service;

import com.songzi.domain.ExamineSubject;
import com.songzi.repository.ExamineSubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@Transactional
public class ExamineSubjectService {

    @Autowired
    private ExamineSubjectRepository examineSubjectRepository;

    /**
     * 统计某年某月 当月题目答题正确次数
     * @param subjectId
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void doExamineSubjectCount(Long subjectId){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String[] localDate = LocalDate.now().format(formatter).split("-");
        String year = localDate[0];
        String month = localDate[1];
        ExamineSubject examineSubject = examineSubjectRepository.findOneBySubjectIdAndYearAndMonth(subjectId,year+"",month+"");
        if(examineSubject != null){
            int examineTimes = examineSubject.getRightTime();
            examineSubject.setRightTime(examineTimes++);
        }else{
            examineSubject = new ExamineSubject();
            examineSubject.setYear(year+"");
            examineSubject.setMonth(month+"");
            examineSubject.setSubjectId(subjectId);
            examineSubject.setRightTime(1);
        }
        examineSubjectRepository.save(examineSubject);
    }
}
