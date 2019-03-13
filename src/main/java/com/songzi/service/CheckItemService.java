package com.songzi.service;

import com.songzi.domain.CheckItem;
import com.songzi.domain.User;
import com.songzi.repository.CheckItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.soap.SOAPBinding;
import java.util.List;


/**
 * Service Implementation for managing CheckItem.
 */
@Service
@Transactional
public class CheckItemService {

    private final Logger log = LoggerFactory.getLogger(CheckItemService.class);

    private final CheckItemRepository checkItemRepository;

    @Autowired private UserService userService;

    public CheckItemService(CheckItemRepository checkItemRepository) {
        this.checkItemRepository = checkItemRepository;
    }

    /**
     * Save a checkItem.
     *
     * @param checkItem the entity to save
     * @return the persisted entity
     */
    public CheckItem save(CheckItem checkItem) {
        log.debug("Request to save CheckItem : {}", checkItem);
        return checkItemRepository.save(checkItem);
    }

    /**
     * Get all the checkItems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CheckItem> findAll(Pageable pageable) {
        log.debug("Request to get all CheckItems");
        return checkItemRepository.findAll(pageable);
    }

    /**
     * Get one checkItem by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public CheckItem findOne(Long id) {
        log.debug("Request to get CheckItem : {}", id);
        return checkItemRepository.findOne(id);
    }

    /**
     * Delete the checkItem by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CheckItem : {}", id);
        checkItemRepository.delete(id);
    }

    /**
     * 根据用户查询自评项
     * @param login
     * @return
     */
    public List<CheckItem> findAllByUser(String login) {
        User user = userService.getUserWithAuthoritiesByLogin(login).get();
        
        return null;
    }
}
