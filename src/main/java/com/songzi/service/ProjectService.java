package com.songzi.service;

import com.songzi.domain.Project;
import com.songzi.domain.enumeration.DeleteFlag;
import com.songzi.domain.enumeration.ExamineStatus;
import com.songzi.domain.enumeration.Status;
import com.songzi.repository.ProjectRepository;
import com.songzi.service.dto.ProjectDTO;
import com.songzi.service.dto.SubjectDTO;
import com.songzi.service.mapper.ProjectMapper;
import com.songzi.service.mapper.ProjectVMMapper;
import com.songzi.service.mapper.SubjectMapper;
import com.songzi.web.rest.errors.BadRequestAlertException;
import com.songzi.web.rest.vm.ProjectQueryVM;
import com.songzi.web.rest.vm.ProjectVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class ProjectService {

    private final Logger log = LoggerFactory.getLogger(ProjectService.class);

    private ProjectRepository projectRepository;
    private UserService userService;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ProjectVMMapper projectVMMapper;

    @Autowired
    private SubjectMapper subjectMapper;

    public ProjectService(ProjectRepository projectRepository, UserService userService) {
        this.projectRepository = projectRepository;
        this.userService = userService;
    }

    /**
     * 新建项目
     *
     * @param projectVM
     * @return
     */
    public Project insert(ProjectVM projectVM) {
        Project project = projectVMMapper.toEntity(projectVM);
        project.setDuration(9900); // 默认设置99分钟
        project.setDelFlag(DeleteFlag.NORMAL);
        project.setStatus(Status.NORMAL);

        return projectRepository.save(project);
    }

    /**
     * 更新项目
     *
     * @param projectVM
     * @return
     */
    public Project update(ProjectVM projectVM) {
        Project project = projectRepository.findOne(projectVM.getId());

        project.setName(projectVM.getName());
        project.setStatus(Status.NORMAL);
        project.setType(projectVM.getType());
        project.setDescription(projectVM.getDescription());
        project.setDuration(projectVM.getDuration());

        return projectRepository.save(project);
    }

    public Page<ProjectDTO> findAll(ProjectQueryVM projectQueryVM, Pageable pageable) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return projectRepository.findAll(new Specification<Project>() {
            @Override
            public Predicate toPredicate(Root<Project> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                if (projectQueryVM.getName() != null && !"".equals(projectQueryVM.getName())) {
                    list.add(cb.like(root.get("name").as(String.class), "%" + projectQueryVM.getName() + "%"));
                }
                if (projectQueryVM.getDate() != null) {
                    list.add(cb.equal(root.get("createdDate").as(LocalDate.class), projectQueryVM.getDate()));
                }
                list.add(cb.equal(root.get("delFlag").as(String.class), DeleteFlag.NORMAL.name()));
                Predicate[] p = new Predicate[list.size()];
                return cb.and(list.toArray(p));
            }
        }, pageRequest).map(projectMapper::toDto);
    }

    /**
     * @param pageable
     * @return
     */
    public Page<Object[]> findAllWithExamine(String projectName, LocalDate localDate, Pageable pageable) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String localDateString;
        Long userId = userService.getCurrentUserId();
        if (projectName == null || "".equals(projectName)) {
            projectName = "%";
        } else {
            projectName = "%" + projectName + "%";
        }

        if (localDate != null) {
            localDateString = localDate.format(dateTimeFormatter);
        } else {
            localDateString = "%";
        }
        int start = pageable.getPageNumber() * pageable.getPageSize();
        List<ProjectDTO> projectDTOList = projectRepository.findAllByDelFlagWithExamineAndCreatedDate(DeleteFlag.NORMAL.name(), userId, Status.PUBLISH.name(), projectName, localDateString, start, pageable.getPageSize())
            .stream().map(objects -> {
                ProjectDTO projectDTO = new ProjectDTO();
                projectDTO.setId(Long.parseLong(objects[0] + ""));
                projectDTO.setName(objects[1] + "");
                projectDTO.setDescription(objects[2] + "");
                Timestamp timestamp = (Timestamp) objects[3];
                projectDTO.setDate(timestamp.toInstant());
                projectDTO.setExamineId(objects[4] == null ? null : Long.parseLong(objects[4] + ""));
                projectDTO.setScore(objects[5] == null ? null : Float.parseFloat((objects[5] + "")));
                projectDTO.setCreatedDate(timestamp.toInstant());
                projectDTO.setCreatedBy(objects[7] + "");
                projectDTO.setDuration(Integer.parseInt(objects[8] + ""));
                projectDTO.setExamineStatus(objects[9] == null ? null : ExamineStatus.valueOf(objects[9] + ""));
                return projectDTO;
            }).collect(Collectors.toList());
        Long count = projectRepository.countAllByDelFlagWithExamineAndCreatedDate(DeleteFlag.NORMAL.name(), userId, Status.PUBLISH.name(), projectName, localDateString);
        PageImpl page = new PageImpl(projectDTOList, pageable, count);
        return page;
    }

    /**
     * @param projectId
     * @param subjectIdList
     */
    public void updateSubject(Long projectId, List<Long> subjectIdList) {
        Project project = projectRepository.findOne(projectId);
        if (project.getStatus() != Status.NORMAL && project.getStatus() != Status.NOTAUDITED) {
            throw new BadRequestAlertException("发布或者审批过的项目，不允许添加题目", this.getClass().getName(), "不允许添加题目");
        }

        //先删除后插入
        projectRepository.deleteProjectSubject(projectId);
        for (Long subjectId : subjectIdList) {
            Long count = projectRepository.getProjectSubject(projectId, subjectId);
            if (count >= 1) {
                continue;
            }
            projectRepository.insertProjectSubject(projectId, subjectId);
        }
    }

    /**
     * 发布
     *
     * @param projectId
     * @return
     */
    public Project publish(Long projectId) {
        Project project = projectRepository.findOne(projectId);
        if (project.getStatus() != Status.AUDITED) {
            throw new BadRequestAlertException("只有审批状态的项目才能发布", this.getClass().getName(), "只有审批状态的项目才能发布");
        }
        project.setStatus(Status.PUBLISH);
        return projectRepository.save(project);
    }

    /**
     * 审批
     *
     * @param projectId
     * @return
     */
    public Project audited(Long projectId, Status status) {
        Project project = projectRepository.findOne(projectId);
        if (project.getStatus() == Status.PUBLISH) {
            throw new BadRequestAlertException("发布过的项目不能再次审批", this.getClass().getName(), "发布过的项目不能再次审批");
        }
        if (status == Status.AUDITED) {
            project.setStatus(Status.AUDITED);
        } else {
            project.setStatus(Status.NOTAUDITED);
        }
        return projectRepository.save(project);
    }

    /**
     * @param id
     * @return
     */
    public ProjectDTO findOne(Long id) {
        Project project = projectRepository.findOne(id);
        ProjectDTO projectDTO = projectMapper.toDto(project);
        List<Object[]> sub = projectRepository.findAllSubjectByProjectId(id);
        List<SubjectDTO> subjectDTOList = new ArrayList<>();
        for (Object[] objects : sub) {
            SubjectDTO subjectDTO = new SubjectDTO();
            subjectDTO.setId(Long.parseLong(objects[0] + ""));
            subjectDTO.setName(objects[1] + "");
            subjectDTO.setTitle(objects[2] + "");
            subjectDTO.setDescription(objects[3] + "");
            subjectDTO.setStatus(objects[4] + "");
            subjectDTO.setType(objects[5] + "");
            subjectDTO.setRight(Long.parseLong(objects[6] + ""));
            subjectDTO.setCreatedBy(objects[7] + "");
            Timestamp timestamp = (Timestamp) objects[8];
            subjectDTO.setCreatedDate(timestamp.toInstant());
            subjectDTOList.add(subjectDTO);
        }
        projectDTO.setSubjectDTOList(subjectDTOList);
        return projectDTO;
    }

    public void delete(Long id) {
        Project project = projectRepository.findOne(id);
        if (project == null) {
            return;
        }
        if (project.getStatus() == Status.PUBLISH) {
            throw new BadRequestAlertException("项目发布后不能删除", this.getClass().getName(), "不能删除");
        } else {
            projectRepository.deleteProjectSubject(id);
            projectRepository.delete(id);
        }
    }
}
