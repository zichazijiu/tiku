package com.songzi.service;

import com.alibaba.fastjson.JSON;
import com.songzi.domain.Subject;
import com.songzi.domain.enumeration.DeleteFlag;
import com.songzi.repository.ProjectRepository;
import com.songzi.repository.SubjectRepository;
import com.songzi.service.dto.MultipleChoice;
import com.songzi.service.dto.SubjectDTO;
import com.songzi.service.mapper.SubjectMapper;
import com.songzi.service.mapper.SubjectVMMapper;
import com.songzi.util.JSONUtils;
import com.songzi.web.rest.errors.BadRequestAlertException;
import com.songzi.web.rest.vm.SubjectQueryVM;
import com.songzi.web.rest.vm.SubjectVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SubjectService {

    @Autowired
    private SubjectMapper subjectMapper;

    @Autowired
    private SubjectVMMapper subjectVMMapper;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ProjectRepository projectRepository;


    /**
     * @param subjectVM
     * @return
     */
    public Subject insert(SubjectVM subjectVM) {
        Subject subject = subjectVMMapper.toEntity(subjectVM);
        subject.setDelFlag(DeleteFlag.NORMAL); // 记录设置为正常

        return subjectRepository.save(subject);
    }

    /**
     * @param subjectVM
     * @return
     */
    public Subject update(SubjectVM subjectVM) {
        Subject subject = subjectRepository.findOne(subjectVM.getId());

        subject.setName(subjectVM.getName());
        subject.setRight(subjectVM.getRight());
        subject.setStatus(subjectVM.getStatus());
        subject.setTitle(subjectVM.getTitle());
        subject.setType(subjectVM.getType());
        subject.setDescription(subjectVM.getDescription());
        subject.setTotalPoint(subjectVM.getTotalPoint()); // 设置总分
        subject.setOptions(subjectVM.getOptions()); // 设置选项

        return subjectRepository.save(subject);
    }

    /**
     * @param subjectQueryVM
     * @param pageable
     * @return
     */
    public Page<SubjectDTO> getAll(SubjectQueryVM subjectQueryVM, Pageable pageable) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return subjectRepository.findAll(new Specification<Subject>() {
            @Override
            public Predicate toPredicate(Root<Subject> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                // 模糊查询名称
                if (subjectQueryVM.getName() != null && !"".equals(subjectQueryVM.getName())) {
                    list.add(cb.like(root.get("name").as(String.class), "%" + subjectQueryVM.getName() + "%"));
                }
                // 创建日期
                if (subjectQueryVM.getDate() != null) {
                    list.add(cb.equal(root.get("createdDate").as(LocalDate.class), subjectQueryVM.getDate()));
                }
                // 删除状态是NORMAL
                list.add(cb.equal(root.get("delFlag").as(String.class), DeleteFlag.NORMAL.name()));

                return cb.and(list.toArray(new Predicate[list.size()]));
            }
        }, pageRequest)
            .map(subjectMapper::toDto)
            .map(subjectDTO -> {
                // 获取项目列表
                List<String> projectList = projectRepository.getProjectNameBySujectId(subjectDTO.getId());
                subjectDTO.setProjectList(projectList);

                // 格式化选项
                String options = subjectDTO.getOptions();
                if (!StringUtils.isEmpty(options)) {
                    // 1. 检查字符串是否是JSON格式
                    boolean isJsonStr = JSONUtils.isJSONFormat(options);
                    // 2. JSON格式转换成对象
                    if (isJsonStr) {
                        // 填充multipleChoices
                        List<MultipleChoice> multipleChoices = JSON.parseArray(options, MultipleChoice.class);
                        subjectDTO.setMultipleChoices(multipleChoices);
                    }
                }


                return subjectDTO;
            });
    }

    public SubjectDTO getOne(Long id) {
        Subject subject = subjectRepository.getOne(id);
        List<String> projectList = projectRepository.getProjectNameBySujectId(subject.getId());
        SubjectDTO subjectDTO = subjectMapper.toDto(subject);
        subjectDTO.setProjectList(projectList);
        // 格式化选项
        subjectDTO.setMultipleChoices(JSON.parseArray(subject.getOptions(),MultipleChoice.class));
        return subjectDTO;
    }

    public void delete(Long id) {
        Long count = subjectRepository.getCountProjecctSubjectBySubjectId(id);

        if (count > 0) {
            throw new BadRequestAlertException("被项目引用的题目不能删除", this.getClass().getName(), "不能删除");
        } else {
            subjectRepository.delete(id);
        }
    }

}
