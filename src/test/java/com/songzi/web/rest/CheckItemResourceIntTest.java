package com.songzi.web.rest;

import com.songzi.TikuApp;

import com.songzi.domain.CheckItem;
import com.songzi.repository.CheckItemRepository;
import com.songzi.service.CheckItemService;
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

import com.songzi.domain.enumeration.CheckItemType;
import com.songzi.domain.enumeration.DeleteFlag;
/**
 * Test class for the CheckItemResource REST controller.
 *
 * @see CheckItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TikuApp.class)
public class CheckItemResourceIntTest {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_PARENT_ID = 1L;
    private static final Long UPDATED_PARENT_ID = 2L;

    private static final CheckItemType DEFAULT_ITEM_TYPE = CheckItemType.MAIN;
    private static final CheckItemType UPDATED_ITEM_TYPE = CheckItemType.SUB;

    private static final DeleteFlag DEFAULT_DEL_FLAG = DeleteFlag.NORMAL;
    private static final DeleteFlag UPDATED_DEL_FLAG = DeleteFlag.DELETE;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private CheckItemRepository checkItemRepository;

    @Autowired
    private CheckItemService checkItemService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCheckItemMockMvc;

    private CheckItem checkItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CheckItemResource checkItemResource = new CheckItemResource(checkItemService);
        this.restCheckItemMockMvc = MockMvcBuilders.standaloneSetup(checkItemResource)
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
    public static CheckItem createEntity(EntityManager em) {
        CheckItem checkItem = new CheckItem()
            .content(DEFAULT_CONTENT)
            .description(DEFAULT_DESCRIPTION)
            .parentId(DEFAULT_PARENT_ID)
            .itemType(DEFAULT_ITEM_TYPE)
            .delFlag(DEFAULT_DEL_FLAG)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE);
        return checkItem;
    }

    @Before
    public void initTest() {
        checkItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createCheckItem() throws Exception {
        int databaseSizeBeforeCreate = checkItemRepository.findAll().size();

        // Create the CheckItem
        restCheckItemMockMvc.perform(post("/api/check-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(checkItem)))
            .andExpect(status().isCreated());

        // Validate the CheckItem in the database
        List<CheckItem> checkItemList = checkItemRepository.findAll();
        assertThat(checkItemList).hasSize(databaseSizeBeforeCreate + 1);
        CheckItem testCheckItem = checkItemList.get(checkItemList.size() - 1);
        assertThat(testCheckItem.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testCheckItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCheckItem.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(testCheckItem.getItemType()).isEqualTo(DEFAULT_ITEM_TYPE);
        assertThat(testCheckItem.getDelFlag()).isEqualTo(DEFAULT_DEL_FLAG);
        assertThat(testCheckItem.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCheckItem.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    public void createCheckItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = checkItemRepository.findAll().size();

        // Create the CheckItem with an existing ID
        checkItem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCheckItemMockMvc.perform(post("/api/check-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(checkItem)))
            .andExpect(status().isBadRequest());

        // Validate the CheckItem in the database
        List<CheckItem> checkItemList = checkItemRepository.findAll();
        assertThat(checkItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCheckItems() throws Exception {
        // Initialize the database
        checkItemRepository.saveAndFlush(checkItem);

        // Get all the checkItemList
        restCheckItemMockMvc.perform(get("/api/check-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(checkItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].parentId").value(hasItem(DEFAULT_PARENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].itemType").value(hasItem(DEFAULT_ITEM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].delFlag").value(hasItem(DEFAULT_DEL_FLAG.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    public void getCheckItem() throws Exception {
        // Initialize the database
        checkItemRepository.saveAndFlush(checkItem);

        // Get the checkItem
        restCheckItemMockMvc.perform(get("/api/check-items/{id}", checkItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(checkItem.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.parentId").value(DEFAULT_PARENT_ID.intValue()))
            .andExpect(jsonPath("$.itemType").value(DEFAULT_ITEM_TYPE.toString()))
            .andExpect(jsonPath("$.delFlag").value(DEFAULT_DEL_FLAG.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCheckItem() throws Exception {
        // Get the checkItem
        restCheckItemMockMvc.perform(get("/api/check-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCheckItem() throws Exception {
        // Initialize the database
        checkItemService.save(checkItem);

        int databaseSizeBeforeUpdate = checkItemRepository.findAll().size();

        // Update the checkItem
        CheckItem updatedCheckItem = checkItemRepository.findOne(checkItem.getId());
        // Disconnect from session so that the updates on updatedCheckItem are not directly saved in db
        em.detach(updatedCheckItem);
        updatedCheckItem
            .content(UPDATED_CONTENT)
            .description(UPDATED_DESCRIPTION)
            .parentId(UPDATED_PARENT_ID)
            .itemType(UPDATED_ITEM_TYPE)
            .delFlag(UPDATED_DEL_FLAG)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restCheckItemMockMvc.perform(put("/api/check-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCheckItem)))
            .andExpect(status().isOk());

        // Validate the CheckItem in the database
        List<CheckItem> checkItemList = checkItemRepository.findAll();
        assertThat(checkItemList).hasSize(databaseSizeBeforeUpdate);
        CheckItem testCheckItem = checkItemList.get(checkItemList.size() - 1);
        assertThat(testCheckItem.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testCheckItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCheckItem.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testCheckItem.getItemType()).isEqualTo(UPDATED_ITEM_TYPE);
        assertThat(testCheckItem.getDelFlag()).isEqualTo(UPDATED_DEL_FLAG);
        assertThat(testCheckItem.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCheckItem.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingCheckItem() throws Exception {
        int databaseSizeBeforeUpdate = checkItemRepository.findAll().size();

        // Create the CheckItem

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCheckItemMockMvc.perform(put("/api/check-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(checkItem)))
            .andExpect(status().isCreated());

        // Validate the CheckItem in the database
        List<CheckItem> checkItemList = checkItemRepository.findAll();
        assertThat(checkItemList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCheckItem() throws Exception {
        // Initialize the database
        checkItemService.save(checkItem);

        int databaseSizeBeforeDelete = checkItemRepository.findAll().size();

        // Get the checkItem
        restCheckItemMockMvc.perform(delete("/api/check-items/{id}", checkItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CheckItem> checkItemList = checkItemRepository.findAll();
        assertThat(checkItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CheckItem.class);
        CheckItem checkItem1 = new CheckItem();
        checkItem1.setId(1L);
        CheckItem checkItem2 = new CheckItem();
        checkItem2.setId(checkItem1.getId());
        assertThat(checkItem1).isEqualTo(checkItem2);
        checkItem2.setId(2L);
        assertThat(checkItem1).isNotEqualTo(checkItem2);
        checkItem1.setId(null);
        assertThat(checkItem1).isNotEqualTo(checkItem2);
    }
}
