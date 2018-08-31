package com.songzi.web.rest;

import com.songzi.TikuApp;
import com.songzi.domain.Examiner;
import com.songzi.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TikuApp.class)
@ActiveProfiles("test")
public class ExaminerResourceTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFooMockMvc;

    private com.songzi.domain.Examiner examiner;

    @Autowired
    private com.songzi.service.ExaminerService examinerService;
    @Autowired
    private com.songzi.repository.ExaminerRepository examinerRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        final ExaminerResource examinerResource = new ExaminerResource(examinerRepository, examinerService);
        this.restFooMockMvc = MockMvcBuilders.standaloneSetup(examinerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    private FormattingConversionService createFormattingConversionService() {
        return new FormattingConversionService();
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Examiner createEntity(EntityManager em) {
        Examiner examiner = new Examiner().name("");
        return examiner;
    }

    @Before
    public void initTest() {
        examiner = createEntity(em);
    }

    @Test
    public void createExaminer() {
    }

    @Test
    public void updateExaminer() {
    }

    @Test
    public void getAllExaminers() throws Exception {
        // Initialize the database
        examinerRepository.saveAndFlush(examiner);

        // Get all the fooList
        restFooMockMvc.perform(get("/api/foos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    @Test
    public void getExaminer() {
    }

    @Test
    public void getCurrentExaminer() {
    }

    @Test
    public void deleteExaminer() {
    }
}
