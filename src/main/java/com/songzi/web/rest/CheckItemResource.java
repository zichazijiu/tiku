package com.songzi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.songzi.domain.CheckItem;
import com.songzi.service.CheckItemService;
import com.songzi.web.rest.errors.BadRequestAlertException;
import com.songzi.web.rest.util.HeaderUtil;
import com.songzi.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CheckItem.
 */
@RestController
@RequestMapping("/api")
public class CheckItemResource {

    private final Logger log = LoggerFactory.getLogger(CheckItemResource.class);

    private static final String ENTITY_NAME = "checkItem";

    private final CheckItemService checkItemService;

    public CheckItemResource(CheckItemService checkItemService) {
        this.checkItemService = checkItemService;
    }

    /**
     * POST  /check-items : Create a new checkItem.
     *
     * @param checkItem the checkItem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new checkItem, or with status 400 (Bad Request) if the checkItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/check-items")
    @Timed
    public ResponseEntity<CheckItem> createCheckItem(@RequestBody CheckItem checkItem) throws URISyntaxException {
        log.debug("REST request to save CheckItem : {}", checkItem);
        if (checkItem.getId() != null) {
            throw new BadRequestAlertException("A new checkItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CheckItem result = checkItemService.save(checkItem);
        return ResponseEntity.created(new URI("/api/check-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /check-items : Updates an existing checkItem.
     *
     * @param checkItem the checkItem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated checkItem,
     * or with status 400 (Bad Request) if the checkItem is not valid,
     * or with status 500 (Internal Server Error) if the checkItem couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/check-items")
    @Timed
    public ResponseEntity<CheckItem> updateCheckItem(@RequestBody CheckItem checkItem) throws URISyntaxException {
        log.debug("REST request to update CheckItem : {}", checkItem);
        if (checkItem.getId() == null) {
            return createCheckItem(checkItem);
        }
        CheckItem result = checkItemService.save(checkItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, checkItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /check-items : get all the checkItems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of checkItems in body
     */
    @GetMapping("/check-items")
    @Timed
    public ResponseEntity<List<CheckItem>> getAllCheckItems(Pageable pageable) {
        log.debug("REST request to get a page of CheckItems");
        Page<CheckItem> page = checkItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/check-items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /check-items/:id : get the "id" checkItem.
     *
     * @param id the id of the checkItem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the checkItem, or with status 404 (Not Found)
     */
    @GetMapping("/check-items/{id}")
    @Timed
    public ResponseEntity<CheckItem> getCheckItem(@PathVariable Long id) {
        log.debug("REST request to get CheckItem : {}", id);
        CheckItem checkItem = checkItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(checkItem));
    }

    /**
     * DELETE  /check-items/:id : delete the "id" checkItem.
     *
     * @param id the id of the checkItem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/check-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteCheckItem(@PathVariable Long id) {
        log.debug("REST request to delete CheckItem : {}", id);
        checkItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
