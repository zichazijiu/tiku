package com.songzi.repository.impl;

import com.songzi.domain.CheckItemAnswer;
import com.songzi.repository.CheckItemAnswerRepositoryCustom;
import com.songzi.service.dto.CheckItemWithAnswerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by Ke Qingyuan on 2019/3/13.
 */
@Repository
public class CheckItemAnswerRepositoryImpl implements CheckItemAnswerRepositoryCustom {
    @Autowired
    EntityManager entityManager;

    /**
     * 根据登录用户名称和部门ID
     *
     * @param login
     * @param deptId
     * @return
     */
    @Override
    public List<CheckItemWithAnswerDTO> findAll(String login, Long deptId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CheckItemAnswer> cq = cb.createQuery(CheckItemAnswer.class);

        Root<CheckItemAnswer> checkItemAnswer = cq.from(CheckItemAnswer.class);
        Predicate createdByPredicate = cb.equal(checkItemAnswer.get("createdBy"), login);
        Predicate deptIdPredicate = cb.equal(checkItemAnswer.get("deptId"), deptId);
        cq.where(createdByPredicate, deptIdPredicate);

        TypedQuery<CheckItemAnswer> query = entityManager.createQuery(cq);
        return null;
    }
}
