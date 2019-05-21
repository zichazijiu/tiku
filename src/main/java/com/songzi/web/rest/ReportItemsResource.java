package com.songzi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.songzi.domain.ReportItems;
import com.songzi.service.ReportItemsService;
import com.songzi.web.rest.errors.BadRequestAlertException;
import com.songzi.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing ReportItems.
 */
@RestController
@RequestMapping("/api")
public class ReportItemsResource {

    private final Logger log = LoggerFactory.getLogger(ReportItemsResource.class);

    private static final String ENTITY_NAME = "reportItems";

    private final ReportItemsService reportItemsService;

    public ReportItemsResource(ReportItemsService reportItemsService) {
        this.reportItemsService = reportItemsService;
    }

    /**
     * POST  /report-items : Create a new reportItems.
     *
     * @param reportItems the reportItems to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reportItems, or with status 400 (Bad Request) if the reportItems has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/report-items")
    @Timed
    public ResponseEntity<ReportItems> createReportItems(@Valid @RequestBody ReportItems reportItems) throws URISyntaxException {
        log.debug("REST request to save ReportItems : {}", reportItems);
        if (reportItems.getId() != null) {
            throw new BadRequestAlertException("A new reportItems cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReportItems result = reportItemsService.save(reportItems);
        return ResponseEntity.created(new URI("/api/report-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /report-items : Updates an existing reportItems.
     *
     * @param reportItems the reportItems to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reportItems,
     * or with status 400 (Bad Request) if the reportItems is not valid,
     * or with status 500 (Internal Server Error) if the reportItems couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/report-items")
    @Timed
    public ResponseEntity<ReportItems> updateReportItems(@Valid @RequestBody ReportItems reportItems) throws URISyntaxException {
        log.debug("REST request to update ReportItems : {}", reportItems);
        if (reportItems.getId() == null) {
            return createReportItems(reportItems);
        }
        ReportItems result = reportItemsService.save(reportItems);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reportItems.getId().toString()))
            .body(result);
    }

    /**
     * GET  /report-items : get all the reportItems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of reportItems in body
     */
    @GetMapping("/report-items")
    @Timed
    public List<ReportItems> getAllReportItems() {
        log.debug("REST request to get all ReportItems");
        return reportItemsService.findAll();
    }

    /**
     * GET  /report-items/:id : get the "id" reportItems.
     *
     * @param id the id of the reportItems to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reportItems, or with status 404 (Not Found)
     */
    @GetMapping("/report-items/{id}")
    @Timed
    public ResponseEntity<ReportItems> getReportItems(@PathVariable Long id) {
        log.debug("REST request to get ReportItems : {}", id);
        ReportItems reportItems = reportItemsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(reportItems));
    }

    /**
     * DELETE  /report-items/:id : delete the "id" reportItems.
     *
     * @param id the id of the reportItems to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/report-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteReportItems(@PathVariable Long id) {
        log.debug("REST request to delete ReportItems : {}", id);
        reportItemsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * COUNT /report-items/getCountByUserId/:login : count based on current users
     *
     * @param login
     * @return
     */
    @GetMapping("/report-items/getCountByUser")
    @Timed
    @ApiOperation("整体自评结果")
    public ResponseEntity<List<Map<String, Object>>> getCountByUserId(@RequestParam("login") String login) {
        log.debug("REST request to count ReportItems");
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(reportItemsService.countByUser(login)));
    }
}
