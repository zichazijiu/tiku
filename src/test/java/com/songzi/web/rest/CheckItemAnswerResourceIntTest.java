package com.songzi.web.rest;

import com.songzi.TikuApp;

import com.songzi.domain.CheckItemAnswer;
import com.songzi.repository.CheckItemAnswerRepository;
import com.songzi.service.CheckItemAnswerService;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.songzi.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CheckItemAnswerResource REST controller.
 *
 * @see CheckItemAnswerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TikuApp.class)
public class CheckItemAnswerResourceIntTest {

    private static final Long DEFAULT_ITEM_ID = 1L;
    private static final Long UPDATED_ITEM_ID = 2L;

    private static final String DEFAULT_YILIU_PROBLEMS = "AAAAAAAAAA";
    private static final String UPDATED_YILIU_PROBLEMS = "BBBBBBBBBB";

    private static final String DEFAULT_ZHENGGAI_INFO = "AAAAAAAAAA";
    private static final String UPDATED_ZHENGGAI_INFO = "BBBBBBBBBB";

    private static final String DEFAULT_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_RESULT = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private CheckItemAnswerRepository checkItemAnswerRepository;

    @Autowired
    private CheckItemAnswerService checkItemAnswerService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCheckItemAnswerMockMvc;

    private CheckItemAnswer checkItemAnswer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CheckItemAnswerResource checkItemAnswerResource = new CheckItemAnswerResource(checkItemAnswerService);
        this.restCheckItemAnswerMockMvc = MockMvcBuilders.standaloneSetup(checkItemAnswerResource)
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
    public static CheckItemAnswer createEntity(EntityManager em) {
        CheckItemAnswer checkItemAnswer = new CheckItemAnswer()
            .itemId(DEFAULT_ITEM_ID)
            .yiliuProblems(DEFAULT_YILIU_PROBLEMS)
            .zhenggaiInfo(DEFAULT_ZHENGGAI_INFO)
            .result(DEFAULT_RESULT)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE);
        return checkItemAnswer;
    }

    @Before
    public void initTest() {
        checkItemAnswer = createEntity(em);
    }

    @Test
    @Transactional
    public void createCheckItemAnswer() throws Exception {
        int databaseSizeBeforeCreate = checkItemAnswerRepository.findAll().size();

        // Create the CheckItemAnswer
        restCheckItemAnswerMockMvc.perform(post("/api/check-item-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(checkItemAnswer)))
            .andExpect(status().isCreated());

        // Validate the CheckItemAnswer in the database
        List<CheckItemAnswer> checkItemAnswerList = checkItemAnswerRepository.findAll();
        assertThat(checkItemAnswerList).hasSize(databaseSizeBeforeCreate + 1);
        CheckItemAnswer testCheckItemAnswer = checkItemAnswerList.get(checkItemAnswerList.size() - 1);
        assertThat(testCheckItemAnswer.getItemId()).isEqualTo(DEFAULT_ITEM_ID);
        assertThat(testCheckItemAnswer.getYiliuProblems()).isEqualTo(DEFAULT_YILIU_PROBLEMS);
        assertThat(testCheckItemAnswer.getZhenggaiInfo()).isEqualTo(DEFAULT_ZHENGGAI_INFO);
        assertThat(testCheckItemAnswer.getResult()).isEqualTo(DEFAULT_RESULT);
        assertThat(testCheckItemAnswer.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCheckItemAnswer.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    public void createCheckItemAnswerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = checkItemAnswerRepository.findAll().size();

        // Create the CheckItemAnswer with an existing ID
        checkItemAnswer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCheckItemAnswerMockMvc.perform(post("/api/check-item-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(checkItemAnswer)))
            .andExpect(status().isBadRequest());

        // Validate the CheckItemAnswer in the database
        List<CheckItemAnswer> checkItemAnswerList = checkItemAnswerRepository.findAll();
        assertThat(checkItemAnswerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCheckItemAnswers() throws Exception {
        // Initialize the database
        checkItemAnswerRepository.saveAndFlush(checkItemAnswer);

        // Get all the checkItemAnswerList
        restCheckItemAnswerMockMvc.perform(get("/api/check-item-answers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(checkItemAnswer.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemId").value(hasItem(DEFAULT_ITEM_ID.intValue())))
            .andExpect(jsonPath("$.[*].yiliuProblems").value(hasItem(DEFAULT_YILIU_PROBLEMS.toString())))
            .andExpect(jsonPath("$.[*].zhenggaiInfo").value(hasItem(DEFAULT_ZHENGGAI_INFO.toString())))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    public void getCheckItemAnswer() throws Exception {
        // Initialize the database
        checkItemAnswerRepository.saveAndFlush(checkItemAnswer);

        // Get the checkItemAnswer
        restCheckItemAnswerMockMvc.perform(get("/api/check-item-answers/{id}", checkItemAnswer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(checkItemAnswer.getId().intValue()))
            .andExpect(jsonPath("$.itemId").value(DEFAULT_ITEM_ID.intValue()))
            .andExpect(jsonPath("$.yiliuProblems").value(DEFAULT_YILIU_PROBLEMS.toString()))
            .andExpect(jsonPath("$.zhenggaiInfo").value(DEFAULT_ZHENGGAI_INFO.toString()))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCheckItemAnswer() throws Exception {
        // Get the checkItemAnswer
        restCheckItemAnswerMockMvc.perform(get("/api/check-item-answers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCheckItemAnswer() throws Exception {
        // Initialize the database
        checkItemAnswerService.save(checkItemAnswer);

        int databaseSizeBeforeUpdate = checkItemAnswerRepository.findAll().size();

        // Update the checkItemAnswer
        CheckItemAnswer updatedCheckItemAnswer = checkItemAnswerRepository.findOne(checkItemAnswer.getId());
        // Disconnect from session so that the updates on updatedCheckItemAnswer are not directly saved in db
        em.detach(updatedCheckItemAnswer);
        updatedCheckItemAnswer
            .itemId(UPDATED_ITEM_ID)
            .yiliuProblems(UPDATED_YILIU_PROBLEMS)
            .zhenggaiInfo(UPDATED_ZHENGGAI_INFO)
            .result(UPDATED_RESULT)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restCheckItemAnswerMockMvc.perform(put("/api/check-item-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCheckItemAnswer)))
            .andExpect(status().isOk());

        // Validate the CheckItemAnswer in the database
        List<CheckItemAnswer> checkItemAnswerList = checkItemAnswerRepository.findAll();
        assertThat(checkItemAnswerList).hasSize(databaseSizeBeforeUpdate);
        CheckItemAnswer testCheckItemAnswer = checkItemAnswerList.get(checkItemAnswerList.size() - 1);
        assertThat(testCheckItemAnswer.getItemId()).isEqualTo(UPDATED_ITEM_ID);
        assertThat(testCheckItemAnswer.getYiliuProblems()).isEqualTo(UPDATED_YILIU_PROBLEMS);
        assertThat(testCheckItemAnswer.getZhenggaiInfo()).isEqualTo(UPDATED_ZHENGGAI_INFO);
        assertThat(testCheckItemAnswer.getResult()).isEqualTo(UPDATED_RESULT);
        assertThat(testCheckItemAnswer.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCheckItemAnswer.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingCheckItemAnswer() throws Exception {
        int databaseSizeBeforeUpdate = checkItemAnswerRepository.findAll().size();

        // Create the CheckItemAnswer

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCheckItemAnswerMockMvc.perform(put("/api/check-item-answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(checkItemAnswer)))
            .andExpect(status().isCreated());

        // Validate the CheckItemAnswer in the database
        List<CheckItemAnswer> checkItemAnswerList = checkItemAnswerRepository.findAll();
        assertThat(checkItemAnswerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCheckItemAnswer() throws Exception {
        // Initialize the database
        checkItemAnswerService.save(checkItemAnswer);

        int databaseSizeBeforeDelete = checkItemAnswerRepository.findAll().size();

        // Get the checkItemAnswer
        restCheckItemAnswerMockMvc.perform(delete("/api/check-item-answers/{id}", checkItemAnswer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CheckItemAnswer> checkItemAnswerList = checkItemAnswerRepository.findAll();
        assertThat(checkItemAnswerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CheckItemAnswer.class);
        CheckItemAnswer checkItemAnswer1 = new CheckItemAnswer();
        checkItemAnswer1.setId(1L);
        CheckItemAnswer checkItemAnswer2 = new CheckItemAnswer();
        checkItemAnswer2.setId(checkItemAnswer1.getId());
        assertThat(checkItemAnswer1).isEqualTo(checkItemAnswer2);
        checkItemAnswer2.setId(2L);
        assertThat(checkItemAnswer1).isNotEqualTo(checkItemAnswer2);
        checkItemAnswer1.setId(null);
        assertThat(checkItemAnswer1).isNotEqualTo(checkItemAnswer2);
    }
}
