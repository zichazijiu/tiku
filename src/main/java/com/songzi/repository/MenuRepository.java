package com.songzi.repository;

import com.songzi.domain.Menu;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Menu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query(value = "SELECT m.* FROM menu m RIGHT JOIN menu_authority ma ON m.id=ma.menu_id WHERE ma.authority_name= ?1", nativeQuery = true)
    List<Menu> findAllWithAuthority(String authority);
}
