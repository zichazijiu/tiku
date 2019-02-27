package com.songzi.web.rest;

import com.songzi.TikuApp;

import com.songzi.domain.Office;
import com.songzi.repository.OfficeRepository;
import com.songzi.service.OfficeService;
import com.songzi.service.dto.OfficeDTO;
import com.songzi.service.mapper.OfficeMapper;
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
 * Test class for the OfficeResource REST controller.
 *
 * @see OfficeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TikuApp.class)
public class OfficeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_OFFICE_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_OFFICE_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_DEL_FLAG = "AAAAAAAAAA";
    private static final String UPDATED_DEL_FLAG = "BBBBBBBBBB";

    private static final String DEFAULT_OFFICE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_OFFICE_TYPE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_LAST_MODIFIED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private OfficeMapper officeMapper;

    @Autowired
    private OfficeService officeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOfficeMockMvc;

    private Office office;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OfficeResource officeResource = new OfficeResource(officeService);
        this.restOfficeMockMvc = MockMvcBuilders.standaloneSetup(officeResource)
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
    public static Office createEntity(EntityManager em) {
        Office office = new Office()
            .name(DEFAULT_NAME)
            .office_status(DEFAULT_OFFICE_STATUS)
            .del_flag(DEFAULT_DEL_FLAG)
            .office_type(DEFAULT_OFFICE_TYPE)
            .created_date(DEFAULT_CREATED_DATE)
            .created_by(DEFAULT_CREATED_BY)
            .last_modified_date(DEFAULT_LAST_MODIFIED_DATE)
            .last_modified_by(DEFAULT_LAST_MODIFIED_BY);
        return office;
    }

    @Before
    public void initTest() {
        office = createEntity(em);
    }

    @Test
    @Transactional
    public void createOffice() throws Exception {
        int databaseSizeBeforeCreate = officeRepository.findAll().size();

        // Create the Office
        OfficeDTO officeDTO = officeMapper.toDto(office);
        restOfficeMockMvc.perform(post("/api/offices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(officeDTO)))
            .andExpect(status().isCreated());

        // Validate the Office in the database
        List<Office> officeList = officeRepository.findAll();
        assertThat(officeList).hasSize(databaseSizeBeforeCreate + 1);
        Office testOffice = officeList.get(officeList.size() - 1);
        assertThat(testOffice.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOffice.getOffice_status()).isEqualTo(DEFAULT_OFFICE_STATUS);
        assertThat(testOffice.getDel_flag()).isEqualTo(DEFAULT_DEL_FLAG);
        assertThat(testOffice.getOffice_type()).isEqualTo(DEFAULT_OFFICE_TYPE);
        assertThat(testOffice.getCreated_date()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testOffice.getCreated_by()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOffice.getLast_modified_date()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testOffice.getLast_modified_by()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void createOfficeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = officeRepository.findAll().size();

        // Create the Office with an existing ID
        office.setId(1L);
        OfficeDTO officeDTO = officeMapper.toDto(office);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOfficeMockMvc.perform(post("/api/offices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(officeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Office in the database
        List<Office> officeList = officeRepository.findAll();
        assertThat(officeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllOffices() throws Exception {
        // Initialize the database
        officeRepository.saveAndFlush(office);

        // Get all the officeList
        restOfficeMockMvc.perform(get("/api/offices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(office.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].office_status").value(hasItem(DEFAULT_OFFICE_STATUS.toString())))
            .andExpect(jsonPath("$.[*].del_flag").value(hasItem(DEFAULT_DEL_FLAG.toString())))
            .andExpect(jsonPath("$.[*].office_type").value(hasItem(DEFAULT_OFFICE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].created_date").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].created_by").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].last_modified_date").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].last_modified_by").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())));
    }

    @Test
    @Transactional
    public void getOffice() throws Exception {
        // Initialize the database
        officeRepository.saveAndFlush(office);

        // Get the office
        restOfficeMockMvc.perform(get("/api/offices/{id}", office.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(office.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.office_status").value(DEFAULT_OFFICE_STATUS.toString()))
            .andExpect(jsonPath("$.del_flag").value(DEFAULT_DEL_FLAG.toString()))
            .andExpect(jsonPath("$.office_type").value(DEFAULT_OFFICE_TYPE.toString()))
            .andExpect(jsonPath("$.created_date").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.created_by").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.last_modified_date").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.last_modified_by").value(DEFAULT_LAST_MODIFIED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOffice() throws Exception {
        // Get the office
        restOfficeMockMvc.perform(get("/api/offices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOffice() throws Exception {
        // Initialize the database
        officeRepository.saveAndFlush(office);
        int databaseSizeBeforeUpdate = officeRepository.findAll().size();

        // Update the office
        Office updatedOffice = officeRepository.findOne(office.getId());
        // Disconnect from session so that the updates on updatedOffice are not directly saved in db
        em.detach(updatedOffice);
        updatedOffice
            .name(UPDATED_NAME)
            .office_status(UPDATED_OFFICE_STATUS)
            .del_flag(UPDATED_DEL_FLAG)
            .office_type(UPDATED_OFFICE_TYPE)
            .created_date(UPDATED_CREATED_DATE)
            .created_by(UPDATED_CREATED_BY)
            .last_modified_date(UPDATED_LAST_MODIFIED_DATE)
            .last_modified_by(UPDATED_LAST_MODIFIED_BY);
        OfficeDTO officeDTO = officeMapper.toDto(updatedOffice);

        restOfficeMockMvc.perform(put("/api/offices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(officeDTO)))
            .andExpect(status().isOk());

        // Validate the Office in the database
        List<Office> officeList = officeRepository.findAll();
        assertThat(officeList).hasSize(databaseSizeBeforeUpdate);
        Office testOffice = officeList.get(officeList.size() - 1);
        assertThat(testOffice.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOffice.getOffice_status()).isEqualTo(UPDATED_OFFICE_STATUS);
        assertThat(testOffice.getDel_flag()).isEqualTo(UPDATED_DEL_FLAG);
        assertThat(testOffice.getOffice_type()).isEqualTo(UPDATED_OFFICE_TYPE);
        assertThat(testOffice.getCreated_date()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testOffice.getCreated_by()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOffice.getLast_modified_date()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testOffice.getLast_modified_by()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingOffice() throws Exception {
        int databaseSizeBeforeUpdate = officeRepository.findAll().size();

        // Create the Office
        OfficeDTO officeDTO = officeMapper.toDto(office);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOfficeMockMvc.perform(put("/api/offices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(officeDTO)))
            .andExpect(status().isCreated());

        // Validate the Office in the database
        List<Office> officeList = officeRepository.findAll();
        assertThat(officeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOffice() throws Exception {
        // Initialize the database
        officeRepository.saveAndFlush(office);
        int databaseSizeBeforeDelete = officeRepository.findAll().size();

        // Get the office
        restOfficeMockMvc.perform(delete("/api/offices/{id}", office.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Office> officeList = officeRepository.findAll();
        assertThat(officeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Office.class);
        Office office1 = new Office();
        office1.setId(1L);
        Office office2 = new Office();
        office2.setId(office1.getId());
        assertThat(office1).isEqualTo(office2);
        office2.setId(2L);
        assertThat(office1).isNotEqualTo(office2);
        office1.setId(null);
        assertThat(office1).isNotEqualTo(office2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OfficeDTO.class);
        OfficeDTO officeDTO1 = new OfficeDTO();
        officeDTO1.setId(1L);
        OfficeDTO officeDTO2 = new OfficeDTO();
        assertThat(officeDTO1).isNotEqualTo(officeDTO2);
        officeDTO2.setId(officeDTO1.getId());
        assertThat(officeDTO1).isEqualTo(officeDTO2);
        officeDTO2.setId(2L);
        assertThat(officeDTO1).isNotEqualTo(officeDTO2);
        officeDTO1.setId(null);
        assertThat(officeDTO1).isNotEqualTo(officeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(officeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(officeMapper.fromId(null)).isNull();
    }
}
