package com.songzi.service;

import com.songzi.domain.Office;
import com.songzi.repository.OfficeRepository;
import com.songzi.service.dto.OfficeDTO;
import com.songzi.service.mapper.OfficeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Office.
 */
@Service
@Transactional
public class OfficeService {

    private final Logger log = LoggerFactory.getLogger(OfficeService.class);

    private final OfficeRepository officeRepository;

    private final OfficeMapper officeMapper;

    public OfficeService(OfficeRepository officeRepository, OfficeMapper officeMapper) {
        this.officeRepository = officeRepository;
        this.officeMapper = officeMapper;
    }

    /**
     * Save a office.
     *
     * @param officeDTO the entity to save
     * @return the persisted entity
     */
    public OfficeDTO save(OfficeDTO officeDTO) {
        log.debug("Request to save Office : {}", officeDTO);
        Office office = officeMapper.toEntity(officeDTO);
        office = officeRepository.save(office);
        return officeMapper.toDto(office);
    }

    /**
     * Get all the offices.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OfficeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Offices");
        return officeRepository.findAll(pageable)
            .map(officeMapper::toDto);
    }

    /**
     * Get one office by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public OfficeDTO findOne(Long id) {
        log.debug("Request to get Office : {}", id);
        Office office = officeRepository.findOne(id);
        return officeMapper.toDto(office);
    }

    /**
     * Delete the office by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Office : {}", id);
        officeRepository.delete(id);
    }
}
