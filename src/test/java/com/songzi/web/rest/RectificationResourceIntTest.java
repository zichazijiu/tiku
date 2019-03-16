package com.songzi.web.rest;

import com.songzi.TikuApp;

import com.songzi.domain.Rectification;
import com.songzi.domain.RemainsQuestion;
import com.songzi.repository.RectificationRepository;
import com.songzi.service.RectificationService;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.songzi.web.rest.TestUtil.sameInstant;
import static com.songzi.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RectificationResource REST controller.
 *
 * @see RectificationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TikuApp.class)
public class RectificationResourceIntTest {

    private static final String DEFAULT_MEASURE = "AAAAAAAAAA";
    private static final String UPDATED_MEASURE = "BBBBBBBBBB";

    private static final String DEFAULT_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_RESULT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_RECTIFICATION_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_RECTIFICATION_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private RectificationRepository rectificationRepository;

    @Autowired
    private RectificationService rectificationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRectificationMockMvc;

    private Rectification rectification;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RectificationResource rectificationResource = new RectificationResource(rectificationService);
        this.restRectificationMockMvc = MockMvcBuilders.standaloneSetup(rectificationResource)
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
    public static Rectification createEntity(EntityManager em) {
        Rectification rectification = new Rectification()
            .measure(DEFAULT_MEASURE)
            .result(DEFAULT_RESULT)
            .rectificationTime(DEFAULT_RECTIFICATION_TIME);
        // Add required entity
        RemainsQuestion remainsQuestion = RemainsQuestionResourceIntTest.createEntity(em);
        em.persist(remainsQuestion);
        em.flush();
        rectification.setRemainsQuestion(remainsQuestion);
        return rectification;
    }

    @Before
    public void initTest() {
        rectification = createEntity(em);
    }

    @Test
    @Transactional
    public void createRectification() throws Exception {
        int databaseSizeBeforeCreate = rectificationRepository.findAll().size();

        // Create the Rectification
        restRectificationMockMvc.perform(post("/api/rectifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rectification)))
            .andExpect(status().isCreated());

        // Validate the Rectification in the database
        List<Rectification> rectificationList = rectificationRepository.findAll();
        assertThat(rectificationList).hasSize(databaseSizeBeforeCreate + 1);
        Rectification testRectification = rectificationList.get(rectificationList.size() - 1);
        assertThat(testRectification.getMeasure()).isEqualTo(DEFAULT_MEASURE);
        assertThat(testRectification.getResult()).isEqualTo(DEFAULT_RESULT);
        assertThat(testRectification.getRectificationTime()).isEqualTo(DEFAULT_RECTIFICATION_TIME);
    }

    @Test
    @Transactional
    public void createRectificationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rectificationRepository.findAll().size();

        // Create the Rectification with an existing ID
        rectification.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRectificationMockMvc.perform(post("/api/rectifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rectification)))
            .andExpect(status().isBadRequest());

        // Validate the Rectification in the database
        List<Rectification> rectificationList = rectificationRepository.findAll();
        assertThat(rectificationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkRectificationTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = rectificationRepository.findAll().size();
        // set the field null
        rectification.setRectificationTime(null);

        // Create the Rectification, which fails.

        restRectificationMockMvc.perform(post("/api/rectifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rectification)))
            .andExpect(status().isBadRequest());

        List<Rectification> rectificationList = rectificationRepository.findAll();
        assertThat(rectificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRectifications() throws Exception {
        // Initialize the database
        rectificationRepository.saveAndFlush(rectification);

        // Get all the rectificationList
        restRectificationMockMvc.perform(get("/api/rectifications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rectification.getId().intValue())))
            .andExpect(jsonPath("$.[*].measure").value(hasItem(DEFAULT_MEASURE.toString())))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT.toString())))
            .andExpect(jsonPath("$.[*].rectificationTime").value(hasItem(sameInstant(DEFAULT_RECTIFICATION_TIME))));
    }

    @Test
    @Transactional
    public void getRectification() throws Exception {
        // Initialize the database
        rectificationRepository.saveAndFlush(rectification);

        // Get the rectification
        restRectificationMockMvc.perform(get("/api/rectifications/{id}", rectification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rectification.getId().intValue()))
            .andExpect(jsonPath("$.measure").value(DEFAULT_MEASURE.toString()))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT.toString()))
            .andExpect(jsonPath("$.rectificationTime").value(sameInstant(DEFAULT_RECTIFICATION_TIME)));
    }

    @Test
    @Transactional
    public void getNonExistingRectification() throws Exception {
        // Get the rectification
        restRectificationMockMvc.perform(get("/api/rectifications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRectification() throws Exception {
        // Initialize the database
        rectificationService.save(rectification);

        int databaseSizeBeforeUpdate = rectificationRepository.findAll().size();

        // Update the rectification
        Rectification updatedRectification = rectificationRepository.findOne(rectification.getId());
        // Disconnect from session so that the updates on updatedRectification are not directly saved in db
        em.detach(updatedRectification);
        updatedRectification
            .measure(UPDATED_MEASURE)
            .result(UPDATED_RESULT)
            .rectificationTime(UPDATED_RECTIFICATION_TIME);

        restRectificationMockMvc.perform(put("/api/rectifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRectification)))
            .andExpect(status().isOk());

        // Validate the Rectification in the database
        List<Rectification> rectificationList = rectificationRepository.findAll();
        assertThat(rectificationList).hasSize(databaseSizeBeforeUpdate);
        Rectification testRectification = rectificationList.get(rectificationList.size() - 1);
        assertThat(testRectification.getMeasure()).isEqualTo(UPDATED_MEASURE);
        assertThat(testRectification.getResult()).isEqualTo(UPDATED_RESULT);
        assertThat(testRectification.getRectificationTime()).isEqualTo(UPDATED_RECTIFICATION_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingRectification() throws Exception {
        int databaseSizeBeforeUpdate = rectificationRepository.findAll().size();

        // Create the Rectification

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRectificationMockMvc.perform(put("/api/rectifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rectification)))
            .andExpect(status().isCreated());

        // Validate the Rectification in the database
        List<Rectification> rectificationList = rectificationRepository.findAll();
        assertThat(rectificationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRectification() throws Exception {
        // Initialize the database
        rectificationService.save(rectification);

        int databaseSizeBeforeDelete = rectificationRepository.findAll().size();

        // Get the rectification
        restRectificationMockMvc.perform(delete("/api/rectifications/{id}", rectification.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Rectification> rectificationList = rectificationRepository.findAll();
        assertThat(rectificationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rectification.class);
        Rectification rectification1 = new Rectification();
        rectification1.setId(1L);
        Rectification rectification2 = new Rectification();
        rectification2.setId(rectification1.getId());
        assertThat(rectification1).isEqualTo(rectification2);
        rectification2.setId(2L);
        assertThat(rectification1).isNotEqualTo(rectification2);
        rectification1.setId(null);
        assertThat(rectification1).isNotEqualTo(rectification2);
    }
}
