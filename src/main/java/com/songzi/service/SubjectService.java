package com.songzi.service;

import com.songzi.domain.Project;
import com.songzi.domain.Subject;
import com.songzi.domain.enumeration.DeleteFlag;
import com.songzi.repository.ProjectRepository;
import com.songzi.repository.SubjectRepository;
import com.songzi.service.dto.ProjectDTO;
import com.songzi.service.dto.SubjectDTO;
import com.songzi.service.mapper.SubjectMapper;
import com.songzi.service.mapper.SubjectVMMapper;
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
     *
     * @param subjectVM
     * @return
     */
    public Subject insert(SubjectVM subjectVM){
        Subject subject = subjectVMMapper.toEntity(subjectVM);

        subject.setDelFlag(DeleteFlag.NORMAL);
        return subjectRepository.save(subject);
    }

    /**
     *
     * @param subjectVM
     * @return
     */
    public Subject update(SubjectVM subjectVM){
        Subject subject = subjectRepository.findOne(subjectVM.getId());

        subject.setName(subjectVM.getName());
        subject.setRight(subjectVM.getRight());
        subject.setStatus(subjectVM.getStatus());
        subject.setTitle(subjectVM.getTitle());
        subject.setType(subjectVM.getType());
        subject.setDescription(subjectVM.getDescription());

        return subjectRepository.save(subject);
    }

    /**
     *
     * @param subjectQueryVM
     * @param pageable
     * @return
     */
    public Page<SubjectDTO> getAll(SubjectQueryVM subjectQueryVM, Pageable pageable){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        PageRequest pageRequest = new PageRequest(pageable.getPageNumber(),pageable.getPageSize(),sort);
        return subjectRepository.findAll(new Specification<Subject>() {
            @Override
            public Predicate toPredicate(Root<Subject> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                if(subjectQueryVM.getName() != null && !"".equals(subjectQueryVM.getName())){
                    list.add(cb.like(root.get("name").as(String.class), "%"+subjectQueryVM.getName()+"%"));
                }
                if(subjectQueryVM.getDate() != null){
                    list.add(cb.equal(root.get("createdDate").as(LocalDate.class), subjectQueryVM.getDate()));
                }
                list.add(cb.equal(root.get("delFlag").as(String.class), DeleteFlag.NORMAL.name()));
                Predicate[] p = new Predicate[list.size()];
                return cb.and(list.toArray(p));
            }
        },pageRequest).map(subjectMapper :: toDto).map(subjectDTO -> {
            List<String> projectList = projectRepository.getProjectNameBySujectId(subjectDTO.getId());
            subjectDTO.setProjectList(projectList);
            return subjectDTO;
        });
    }

    public SubjectDTO getOne(Long id){
        Subject subject = subjectRepository.getOne(id);
        List<String> projectList = projectRepository.getProjectNameBySujectId(subject.getId());
        SubjectDTO subjectDTO = subjectMapper.toDto(subject);
        subjectDTO.setProjectList(projectList);
        return subjectDTO;
    }

    public void delete(Long id){
        Long count = subjectRepository.getCountProjecctSubjectBySubjectId(id);

        if(count > 0){
            throw new BadRequestAlertException("被项目引用的题目不能删除",this.getClass().getName(),"不能删除");
        }else{
            subjectRepository.delete(id);
        }
    }

}
