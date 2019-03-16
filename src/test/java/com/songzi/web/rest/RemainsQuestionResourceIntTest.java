package com.songzi.web.rest;

import com.songzi.TikuApp;

import com.songzi.domain.RemainsQuestion;
import com.songzi.domain.ReportItems;
import com.songzi.repository.RemainsQuestionRepository;
import com.songzi.service.RemainsQuestionService;
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

import com.songzi.domain.enumeration.QuestionType;
/**
 * Test class for the RemainsQuestionResource REST controller.
 *
 * @see RemainsQuestionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TikuApp.class)
public class RemainsQuestionResourceIntTest {

    private static final QuestionType DEFAULT_QUESTION_TYPE = QuestionType.LESS_MANAGE;
    private static final QuestionType UPDATED_QUESTION_TYPE = QuestionType.SECRET_RISK;

    private static final ZonedDateTime DEFAULT_CREATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private RemainsQuestionRepository remainsQuestionRepository;

    @Autowired
    private RemainsQuestionService remainsQuestionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRemainsQuestionMockMvc;

    private RemainsQuestion remainsQuestion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RemainsQuestionResource remainsQuestionResource = new RemainsQuestionResource(remainsQuestionService);
        this.restRemainsQuestionMockMvc = MockMvcBuilders.standaloneSetup(remainsQuestionResource)
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
    public static RemainsQuestion createEntity(EntityManager em) {
        RemainsQuestion remainsQuestion = new RemainsQuestion()
            .questionType(DEFAULT_QUESTION_TYPE)
            .createdTime(DEFAULT_CREATED_TIME)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        ReportItems reportItems = ReportItemsResourceIntTest.createEntity(em);
        em.persist(reportItems);
        em.flush();
        remainsQuestion.setReportItems(reportItems);
        return remainsQuestion;
    }

    @Before
    public void initTest() {
        remainsQuestion = createEntity(em);
    }

    @Test
    @Transactional
    public void createRemainsQuestion() throws Exception {
        int databaseSizeBeforeCreate = remainsQuestionRepository.findAll().size();

        // Create the RemainsQuestion
        restRemainsQuestionMockMvc.perform(post("/api/remains-questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(remainsQuestion)))
            .andExpect(status().isCreated());

        // Validate the RemainsQuestion in the database
        List<RemainsQuestion> remainsQuestionList = remainsQuestionRepository.findAll();
        assertThat(remainsQuestionList).hasSize(databaseSizeBeforeCreate + 1);
        RemainsQuestion testRemainsQuestion = remainsQuestionList.get(remainsQuestionList.size() - 1);
        assertThat(testRemainsQuestion.getQuestionType()).isEqualTo(DEFAULT_QUESTION_TYPE);
        assertThat(testRemainsQuestion.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testRemainsQuestion.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createRemainsQuestionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = remainsQuestionRepository.findAll().size();

        // Create the RemainsQuestion with an existing ID
        remainsQuestion.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRemainsQuestionMockMvc.perform(post("/api/remains-questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(remainsQuestion)))
            .andExpect(status().isBadRequest());

        // Validate the RemainsQuestion in the database
        List<RemainsQuestion> remainsQuestionList = remainsQuestionRepository.findAll();
        assertThat(remainsQuestionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkQuestionTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = remainsQuestionRepository.findAll().size();
        // set the field null
        remainsQuestion.setQuestionType(null);

        // Create the RemainsQuestion, which fails.

        restRemainsQuestionMockMvc.perform(post("/api/remains-questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(remainsQuestion)))
            .andExpect(status().isBadRequest());

        List<RemainsQuestion> remainsQuestionList = remainsQuestionRepository.findAll();
        assertThat(remainsQuestionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = remainsQuestionRepository.findAll().size();
        // set the field null
        remainsQuestion.setCreatedTime(null);

        // Create the RemainsQuestion, which fails.

        restRemainsQuestionMockMvc.perform(post("/api/remains-questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(remainsQuestion)))
            .andExpect(status().isBadRequest());

        List<RemainsQuestion> remainsQuestionList = remainsQuestionRepository.findAll();
        assertThat(remainsQuestionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRemainsQuestions() throws Exception {
        // Initialize the database
        remainsQuestionRepository.saveAndFlush(remainsQuestion);

        // Get all the remainsQuestionList
        restRemainsQuestionMockMvc.perform(get("/api/remains-questions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(remainsQuestion.getId().intValue())))
            .andExpect(jsonPath("$.[*].questionType").value(hasItem(DEFAULT_QUESTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(sameInstant(DEFAULT_CREATED_TIME))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getRemainsQuestion() throws Exception {
        // Initialize the database
        remainsQuestionRepository.saveAndFlush(remainsQuestion);

        // Get the remainsQuestion
        restRemainsQuestionMockMvc.perform(get("/api/remains-questions/{id}", remainsQuestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(remainsQuestion.getId().intValue()))
            .andExpect(jsonPath("$.questionType").value(DEFAULT_QUESTION_TYPE.toString()))
            .andExpect(jsonPath("$.createdTime").value(sameInstant(DEFAULT_CREATED_TIME)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRemainsQuestion() throws Exception {
        // Get the remainsQuestion
        restRemainsQuestionMockMvc.perform(get("/api/remains-questions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRemainsQuestion() throws Exception {
        // Initialize the database
        remainsQuestionService.save(remainsQuestion);

        int databaseSizeBeforeUpdate = remainsQuestionRepository.findAll().size();

        // Update the remainsQuestion
        RemainsQuestion updatedRemainsQuestion = remainsQuestionRepository.findOne(remainsQuestion.getId());
        // Disconnect from session so that the updates on updatedRemainsQuestion are not directly saved in db
        em.detach(updatedRemainsQuestion);
        updatedRemainsQuestion
            .questionType(UPDATED_QUESTION_TYPE)
            .createdTime(UPDATED_CREATED_TIME)
            .description(UPDATED_DESCRIPTION);

        restRemainsQuestionMockMvc.perform(put("/api/remains-questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRemainsQuestion)))
            .andExpect(status().isOk());

        // Validate the RemainsQuestion in the database
        List<RemainsQuestion> remainsQuestionList = remainsQuestionRepository.findAll();
        assertThat(remainsQuestionList).hasSize(databaseSizeBeforeUpdate);
        RemainsQuestion testRemainsQuestion = remainsQuestionList.get(remainsQuestionList.size() - 1);
        assertThat(testRemainsQuestion.getQuestionType()).isEqualTo(UPDATED_QUESTION_TYPE);
        assertThat(testRemainsQuestion.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testRemainsQuestion.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingRemainsQuestion() throws Exception {
        int databaseSizeBeforeUpdate = remainsQuestionRepository.findAll().size();

        // Create the RemainsQuestion

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRemainsQuestionMockMvc.perform(put("/api/remains-questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(remainsQuestion)))
            .andExpect(status().isCreated());

        // Validate the RemainsQuestion in the database
        List<RemainsQuestion> remainsQuestionList = remainsQuestionRepository.findAll();
        assertThat(remainsQuestionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRemainsQuestion() throws Exception {
        // Initialize the database
        remainsQuestionService.save(remainsQuestion);

        int databaseSizeBeforeDelete = remainsQuestionRepository.findAll().size();

        // Get the remainsQuestion
        restRemainsQuestionMockMvc.perform(delete("/api/remains-questions/{id}", remainsQuestion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RemainsQuestion> remainsQuestionList = remainsQuestionRepository.findAll();
        assertThat(remainsQuestionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RemainsQuestion.class);
        RemainsQuestion remainsQuestion1 = new RemainsQuestion();
        remainsQuestion1.setId(1L);
        RemainsQuestion remainsQuestion2 = new RemainsQuestion();
        remainsQuestion2.setId(remainsQuestion1.getId());
        assertThat(remainsQuestion1).isEqualTo(remainsQuestion2);
        remainsQuestion2.setId(2L);
        assertThat(remainsQuestion1).isNotEqualTo(remainsQuestion2);
        remainsQuestion1.setId(null);
        assertThat(remainsQuestion1).isNotEqualTo(remainsQuestion2);
    }
}
