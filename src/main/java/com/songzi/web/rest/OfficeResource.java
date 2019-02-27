package com.songzi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.songzi.service.OfficeService;
import com.songzi.web.rest.errors.BadRequestAlertException;
import com.songzi.web.rest.util.HeaderUtil;
import com.songzi.web.rest.util.PaginationUtil;
import com.songzi.service.dto.OfficeDTO;
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
 * REST controller for managing Office.
 */
@RestController
@RequestMapping("/api")
public class OfficeResource {

    private final Logger log = LoggerFactory.getLogger(OfficeResource.class);

    private static final String ENTITY_NAME = "office";

    private final OfficeService officeService;

    public OfficeResource(OfficeService officeService) {
        this.officeService = officeService;
    }

    /**
     * POST  /offices : Create a new office.
     *
     * @param officeDTO the officeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new officeDTO, or with status 400 (Bad Request) if the office has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/offices")
    @Timed
    public ResponseEntity<OfficeDTO> createOffice(@RequestBody OfficeDTO officeDTO) throws URISyntaxException {
        log.debug("REST request to save Office : {}", officeDTO);
        if (officeDTO.getId() != null) {
            throw new BadRequestAlertException("A new office cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OfficeDTO result = officeService.save(officeDTO);
        return ResponseEntity.created(new URI("/api/offices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /offices : Updates an existing office.
     *
     * @param officeDTO the officeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated officeDTO,
     * or with status 400 (Bad Request) if the officeDTO is not valid,
     * or with status 500 (Internal Server Error) if the officeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/offices")
    @Timed
    public ResponseEntity<OfficeDTO> updateOffice(@RequestBody OfficeDTO officeDTO) throws URISyntaxException {
        log.debug("REST request to update Office : {}", officeDTO);
        if (officeDTO.getId() == null) {
            return createOffice(officeDTO);
        }
        OfficeDTO result = officeService.save(officeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, officeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /offices : get all the offices.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of offices in body
     */
    @GetMapping("/offices")
    @Timed
    public ResponseEntity<List<OfficeDTO>> getAllOffices(Pageable pageable) {
        log.debug("REST request to get a page of Offices");
        Page<OfficeDTO> page = officeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/offices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /offices/:id : get the "id" office.
     *
     * @param id the id of the officeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the officeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/offices/{id}")
    @Timed
    public ResponseEntity<OfficeDTO> getOffice(@PathVariable Long id) {
        log.debug("REST request to get Office : {}", id);
        OfficeDTO officeDTO = officeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(officeDTO));
    }

    /**
     * DELETE  /offices/:id : delete the "id" office.
     *
     * @param id the id of the officeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/offices/{id}")
    @Timed
    public ResponseEntity<Void> deleteOffice(@PathVariable Long id) {
        log.debug("REST request to delete Office : {}", id);
        officeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
