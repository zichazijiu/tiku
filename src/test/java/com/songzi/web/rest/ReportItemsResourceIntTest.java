package com.songzi.web.rest;

import com.songzi.TikuApp;

import com.songzi.domain.ReportItems;
import com.songzi.domain.Report;
import com.songzi.repository.ReportItemsRepository;
import com.songzi.service.ReportItemsService;
import com.songzi.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.songzi.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ReportItemsResource REST controller.
 *
 * @see ReportItemsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TikuApp.class)
public class ReportItemsResourceIntTest {

    private static final String DEFAULT_LEVEL = "AAAAAAAAAA";
    private static final String UPDATED_LEVEL = "BBBBBBBBBB";

    @Autowired
    private ReportItemsRepository reportItemsRepository;

    @Autowired
    private ReportItemsService reportItemsService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReportItemsMockMvc;

    private ReportItems reportItems;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReportItemsResource reportItemsResource = new ReportItemsResource(reportItemsService);
        this.restReportItemsMockMvc = MockMvcBuilders.standaloneSetup(reportItemsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportItems createEntity(EntityManager em) {
        ReportItems reportItems = new ReportItems()
            .level(DEFAULT_LEVEL);
        // Add required entity
        //Report report = ReportResourceIntTest.createEntity(em);
        Report report = new Report();
        em.persist(report);
        em.flush();
        reportItems.setReport(report);
        return reportItems;
    }

    @Before
    public void initTest() {
        reportItems = createEntity(em);
    }

    @Test
    @Transactional
    public void createReportItems() throws Exception {
        int databaseSizeBeforeCreate = reportItemsRepository.findAll().size();

        // Create the ReportItems
        restReportItemsMockMvc.perform(post("/api/report-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportItems)))
            .andExpect(status().isCreated());

        // Validate the ReportItems in the database
        List<ReportItems> reportItemsList = reportItemsRepository.findAll();
        assertThat(reportItemsList).hasSize(databaseSizeBeforeCreate + 1);
        ReportItems testReportItems = reportItemsList.get(reportItemsList.size() - 1);
        assertThat(testReportItems.getLevel()).isEqualTo(DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    public void createReportItemsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reportItemsRepository.findAll().size();

        // Create the ReportItems with an existing ID
        reportItems.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportItemsMockMvc.perform(post("/api/report-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportItems)))
            .andExpect(status().isBadRequest());

        // Validate the ReportItems in the database
        List<ReportItems> reportItemsList = reportItemsRepository.findAll();
        assertThat(reportItemsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllReportItems() throws Exception {
        // Initialize the database
        reportItemsRepository.saveAndFlush(reportItems);

        // Get all the reportItemsList
        restReportItemsMockMvc.perform(get("/api/report-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportItems.getId().intValue())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())));
    }

    @Test
    @Transactional
    public void getReportItems() throws Exception {
        // Initialize the database
        reportItemsRepository.saveAndFlush(reportItems);

        // Get the reportItems
        restReportItemsMockMvc.perform(get("/api/report-items/{id}", reportItems.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reportItems.getId().intValue()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReportItems() throws Exception {
        // Get the reportItems
        restReportItemsMockMvc.perform(get("/api/report-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReportItems() throws Exception {
        // Initialize the database
        reportItemsService.save(reportItems);

        int databaseSizeBeforeUpdate = reportItemsRepository.findAll().size();

        // Update the reportItems
        ReportItems updatedReportItems = reportItemsRepository.findOne(reportItems.getId());
        // Disconnect from session so that the updates on updatedReportItems are not directly saved in db
        em.detach(updatedReportItems);
        updatedReportItems
            .level(UPDATED_LEVEL);

        restReportItemsMockMvc.perform(put("/api/report-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReportItems)))
            .andExpect(status().isOk());

        // Validate the ReportItems in the database
        List<ReportItems> reportItemsList = reportItemsRepository.findAll();
        assertThat(reportItemsList).hasSize(databaseSizeBeforeUpdate);
        ReportItems testReportItems = reportItemsList.get(reportItemsList.size() - 1);
        assertThat(testReportItems.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void updateNonExistingReportItems() throws Exception {
        int databaseSizeBeforeUpdate = reportItemsRepository.findAll().size();

        // Create the ReportItems

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReportItemsMockMvc.perform(put("/api/report-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reportItems)))
            .andExpect(status().isCreated());

        // Validate the ReportItems in the database
        List<ReportItems> reportItemsList = reportItemsRepository.findAll();
        assertThat(reportItemsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReportItems() throws Exception {
        // Initialize the database
        reportItemsService.save(reportItems);

        int databaseSizeBeforeDelete = reportItemsRepository.findAll().size();

        // Get the reportItems
        restReportItemsMockMvc.perform(delete("/api/report-items/{id}", reportItems.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ReportItems> reportItemsList = reportItemsRepository.findAll();
        assertThat(reportItemsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportItems.class);
        ReportItems reportItems1 = new ReportItems();
        reportItems1.setId(1L);
        ReportItems reportItems2 = new ReportItems();
        reportItems2.setId(reportItems1.getId());
        assertThat(reportItems1).isEqualTo(reportItems2);
        reportItems2.setId(2L);
        assertThat(reportItems1).isNotEqualTo(reportItems2);
        reportItems1.setId(null);
        assertThat(reportItems1).isNotEqualTo(reportItems2);
    }
}
