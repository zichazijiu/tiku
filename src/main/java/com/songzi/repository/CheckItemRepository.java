package com.songzi.repository;

import com.songzi.domain.CheckItem;
import com.songzi.domain.enumeration.CheckItemType;
import com.songzi.domain.enumeration.DeleteFlag;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the CheckItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CheckItemRepository extends JpaRepository<CheckItem, Long> {
    /**
     * 根据删除状态和自查题目状态查找列表
     * @param deleteFlag
     * @param checkItemType
     * @return
     */
    List<CheckItem> findAllByDelFlagIsAndItemTypeIs(DeleteFlag deleteFlag, CheckItemType checkItemType);

    /**
     * 根据删除状态和父亲ID查列表
     * @param deleteFlag
     * @param parentId
     * @return
     */
    List<CheckItem> findAllByDelFlagAndParentId(DeleteFlag deleteFlag, Long parentId);
}
