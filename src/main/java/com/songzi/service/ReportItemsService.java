package com.songzi.service;

import com.songzi.domain.Report;
import com.songzi.domain.ReportItems;
import com.songzi.repository.ReportItemsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing ReportItems.
 */
@Service
@Transactional
public class ReportItemsService {

    private final Logger log = LoggerFactory.getLogger(ReportItemsService.class);

    private final ReportItemsRepository reportItemsRepository;

    public ReportItemsService(ReportItemsRepository reportItemsRepository) {
        this.reportItemsRepository = reportItemsRepository;
    }

    /**
     * Save a reportItems.
     *
     * @param reportItems the entity to save
     * @return the persisted entity
     */
    public ReportItems save(ReportItems reportItems) {
        log.debug("Request to save ReportItems : {}", reportItems);
        return reportItemsRepository.save(reportItems);
    }

    /**
     * Get all the reportItems.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ReportItems> findAll() {
        log.debug("Request to get all ReportItems");
        return reportItemsRepository.findAll();
    }

    /**
     * Get one reportItems by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ReportItems findOne(Long id) {
        log.debug("Request to get ReportItems : {}", id);
        return reportItemsRepository.findOne(id);
    }

    /**
     * Delete the reportItems by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ReportItems : {}", id);
        reportItemsRepository.delete(id);
    }

    /**
     * 根据报告获取报告详情
     * @param report
     * @return
     */
    public List<ReportItems> findAllByReport(Report report) {
        return reportItemsRepository.findAllByReport(report);
    }
}
