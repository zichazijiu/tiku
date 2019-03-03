package com.songzi.repository;

import com.songzi.domain.CheckItemAnswer;
import com.songzi.domain.CheckItemAnswerWithItem;
import com.songzi.domain.Examine;
import com.songzi.service.dto.CheckItemAnswerDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data JPA repository for the CheckItemAnswer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CheckItemAnswerRepository extends JpaRepository<CheckItemAnswer, Long> {

    @Query("select new com.songzi.domain.CheckItemAnswerWithItem(ci.id,ci.content,ci.description,cia.id, cia.result, cia.yiliuProblems, cia.zhenggaiInfo, cia.createdBy) from CheckItemAnswer cia left join CheckItem ci on cia.itemId = ci.id WHERE cia.createdBy=:user")
    List<CheckItemAnswerWithItem> listCheckAnswerWithItem(@Param("user")String user);

}
