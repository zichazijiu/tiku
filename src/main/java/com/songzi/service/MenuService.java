package com.songzi.service;

import com.songzi.domain.Menu;
import com.songzi.repository.MenuRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing Menu.
 */
@Service
@Transactional
public class MenuService {

    private final Logger log = LoggerFactory.getLogger(MenuService.class);

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    /**
     * Save a menu.
     *
     * @param menu the entity to save
     * @return the persisted entity
     */
    public Menu save(Menu menu) {
        log.debug("Request to save Menu : {}", menu);
        return menuRepository.save(menu);
    }

    /**
     * Get all the menus.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Menu> findAll() {
        log.debug("Request to get all Menus");
        return menuRepository.findAll();
    }

    /**
     * Get one menu by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Menu findOne(Long id) {
        log.debug("Request to get Menu : {}", id);
        return menuRepository.findOne(id);
    }

    /**
     * Delete the menu by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Menu : {}", id);
        menuRepository.delete(id);
    }
}
