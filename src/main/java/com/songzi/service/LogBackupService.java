package com.songzi.service;

import com.songzi.domain.LogBackup;
import com.songzi.domain.enumeration.BakType;
import com.songzi.domain.enumeration.Level;
import com.songzi.domain.enumeration.LogType;
import com.songzi.repository.LogBackupRepository;
import com.songzi.service.dto.LogBackupDTO;
import com.songzi.service.mapper.LogBackupMapper;
import com.songzi.web.rest.vm.DataBackupQueryVM;
import com.songzi.web.rest.vm.LogBackupQueryVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LogBackupService {

    @Autowired
    private LogBackupRepository logBackupRepository;

    @Autowired
    private LogBackupMapper logBackupMapper;

    public Page<LogBackupDTO> getAllLogBackups(LogBackupQueryVM logBackupQueryVM, Pageable pageable) {
        return logBackupRepository.findAll(new Specification<LogBackup>() {
            @Override
            public Predicate toPredicate(Root<LogBackup> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                if(logBackupQueryVM.getDescription() != null){
                    list.add(cb.like(root.get("description").as(String.class), "%"+logBackupQueryVM.getDescription()+"%"));
                }

                if(logBackupQueryVM.getLevel() != null){
                    list.add(cb.equal(root.get("level").as(Level.class), logBackupQueryVM.getLevel()));
                }

                if(logBackupQueryVM.getCreatedStartDate() != null && logBackupQueryVM.getCreatedStartTime() != null){
                    String[] local = logBackupQueryVM.getCreatedStartTime().split(":");
                    LocalTime localTime = LocalTime.of(Integer.parseInt(local[0]),Integer.parseInt(local[1]),Integer.parseInt(local[2]));
                    list.add(cb.greaterThanOrEqualTo(root.get("createdTime").as(LocalDateTime.class), LocalDateTime.of(logBackupQueryVM.getCreatedStartDate(),localTime)));
                }

                if(logBackupQueryVM.getCreatedEndDate() != null && logBackupQueryVM.getCreatedEndTime() != null){
                    String[] local = logBackupQueryVM.getCreatedEndTime().split(":");
                    LocalTime localTime = LocalTime.of(Integer.parseInt(local[0]),Integer.parseInt(local[1]),Integer.parseInt(local[2]));
                    list.add(cb.lessThanOrEqualTo(root.get("createdTime").as(LocalDateTime.class), LocalDateTime.of(logBackupQueryVM.getCreatedEndDate(),localTime)));
                }

                list.add(cb.equal(root.get("logType").as(LogType.class), LogType.SECURITY));
                Predicate[] p = new Predicate[list.size()];
                return cb.and(list.toArray(p));
            }
        },pageable).map(logBackupMapper::toDto);
    }

    public Page<LogBackupDTO> getAllIEmportBackups(DataBackupQueryVM dataBackupQueryVM, Pageable pageable) {
        return logBackupRepository.findAll(new Specification<LogBackup>() {
            @Override
            public Predicate toPredicate(Root<LogBackup> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                if(dataBackupQueryVM.getCreatedTime() != null){
                    list.add(cb.equal(root.get("createdTime").as(LocalDate.class), dataBackupQueryVM.getCreatedTime()));
                }

                if(dataBackupQueryVM.getBakType() != null){
                    list.add(cb.equal(root.get("bakType").as(BakType.class), dataBackupQueryVM.getBakType()));
                }

                if(dataBackupQueryVM.getDescription() != null){
                    list.add(cb.equal(root.get("description").as(String.class), dataBackupQueryVM.getDescription()));
                }

                list.add(cb.equal(root.get("logType").as(LogType.class), LogType.IEMPORT));
                Predicate[] p = new Predicate[list.size()];
                return cb.and(list.toArray(p));
            }
        },pageable).map(logBackupMapper :: toDto);
    }

    public Page<LogBackupDTO> getAllDatabaseBackups(DataBackupQueryVM dataBackupQueryVM, Pageable pageable) {
        return logBackupRepository.findAll(new Specification<LogBackup>() {
            @Override
            public Predicate toPredicate(Root<LogBackup> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                if(dataBackupQueryVM.getCreatedTime() != null){
                    list.add(cb.equal(root.get("createdTime").as(LocalDate.class), dataBackupQueryVM.getCreatedTime()));
                }

                if(dataBackupQueryVM.getBakType() != null){
                    list.add(cb.equal(root.get("bakType").as(BakType.class), dataBackupQueryVM.getBakType()));
                }

                if(dataBackupQueryVM.getDescription() != null){
                    list.add(cb.equal(root.get("description").as(String.class), dataBackupQueryVM.getDescription()));
                }
                list.add(cb.equal(root.get("logType").as(LogType.class), LogType.DATABASE));
                Predicate[] p = new Predicate[list.size()];
                return cb.and(list.toArray(p));
            }
        },pageable).map(logBackupMapper :: toDto);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insert(LogBackup logBackup){
        logBackupRepository.save(logBackup);
    }
}
