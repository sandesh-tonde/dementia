package com.db.ppt.dementia.web.rest;

import static com.db.ppt.dementia.domain.BlogAsserts.*;
import static com.db.ppt.dementia.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.db.ppt.dementia.IntegrationTest;
import com.db.ppt.dementia.domain.Blog;
import com.db.ppt.dementia.domain.User;
import com.db.ppt.dementia.repository.BlogRepository;
import com.db.ppt.dementia.repository.UserRepository;
import com.db.ppt.dementia.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BlogResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BlogResourceIT {

    private static final String DEFAULT_CONTENT =
        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CONTENT =
        "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_VOTE = 1;
    private static final Integer UPDATED_VOTE = 2;

    private static final String ENTITY_API_URL = "/api/blogs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private BlogRepository blogRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBlogMockMvc;

    private Blog blog;

    private Blog insertedBlog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Blog createEntity(EntityManager em) {
        Blog blog = new Blog().content(DEFAULT_CONTENT).dateTime(DEFAULT_DATE_TIME).vote(DEFAULT_VOTE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        blog.setAuther(user);
        return blog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Blog createUpdatedEntity(EntityManager em) {
        Blog blog = new Blog().content(UPDATED_CONTENT).dateTime(UPDATED_DATE_TIME).vote(UPDATED_VOTE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        blog.setAuther(user);
        return blog;
    }

    @BeforeEach
    public void initTest() {
        blog = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedBlog != null) {
            blogRepository.delete(insertedBlog);
            insertedBlog = null;
        }
    }

    @Test
    @Transactional
    void createBlog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Blog
        var returnedBlog = om.readValue(
            restBlogMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(blog)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Blog.class
        );

        // Validate the Blog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertBlogUpdatableFieldsEquals(returnedBlog, getPersistedBlog(returnedBlog));

        assertBlogMapsIdRelationshipPersistedValue(blog, returnedBlog);

        insertedBlog = returnedBlog;
    }

    @Test
    @Transactional
    void createBlogWithExistingId() throws Exception {
        // Create the Blog with an existing ID
        blog.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBlogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(blog)))
            .andExpect(status().isBadRequest());

        // Validate the Blog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void updateBlogMapsIdAssociationWithNewId() throws Exception {
        // Initialize the database
        insertedBlog = blogRepository.saveAndFlush(blog);
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Add a new parent entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();

        // Load the blog
        Blog updatedBlog = blogRepository.findById(blog.getId()).orElseThrow();
        assertThat(updatedBlog).isNotNull();
        // Disconnect from session so that the updates on updatedBlog are not directly saved in db
        em.detach(updatedBlog);

        // Update the User with new association value
        updatedBlog.setAuther(user);

        // Update the entity
        restBlogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBlog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedBlog))
            )
            .andExpect(status().isOk());

        // Validate the Blog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        /**
         * Validate the id for MapsId, the ids must be same
         * Uncomment the following line for assertion. However, please note that there is a known issue and uncommenting will fail the test.
         * Please look at https://github.com/jhipster/generator-jhipster/issues/9100. You can modify this test as necessary.
         * assertThat(testBlog.getId()).isEqualTo(testBlog.getUser().getId());
         */
    }

    @Test
    @Transactional
    void checkContentIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        blog.setContent(null);

        // Create the Blog, which fails.

        restBlogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(blog)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        blog.setDateTime(null);

        // Create the Blog, which fails.

        restBlogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(blog)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBlogs() throws Exception {
        // Initialize the database
        insertedBlog = blogRepository.saveAndFlush(blog);

        // Get all the blogList
        restBlogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(blog.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].dateTime").value(hasItem(DEFAULT_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].vote").value(hasItem(DEFAULT_VOTE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBlogsWithEagerRelationshipsIsEnabled() throws Exception {
        when(blogRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBlogMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(blogRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBlogsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(blogRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBlogMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(blogRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBlog() throws Exception {
        // Initialize the database
        insertedBlog = blogRepository.saveAndFlush(blog);

        // Get the blog
        restBlogMockMvc
            .perform(get(ENTITY_API_URL_ID, blog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(blog.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.dateTime").value(DEFAULT_DATE_TIME.toString()))
            .andExpect(jsonPath("$.vote").value(DEFAULT_VOTE));
    }

    @Test
    @Transactional
    void getNonExistingBlog() throws Exception {
        // Get the blog
        restBlogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBlog() throws Exception {
        // Initialize the database
        insertedBlog = blogRepository.saveAndFlush(blog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the blog
        Blog updatedBlog = blogRepository.findById(blog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBlog are not directly saved in db
        em.detach(updatedBlog);
        updatedBlog.content(UPDATED_CONTENT).dateTime(UPDATED_DATE_TIME).vote(UPDATED_VOTE);

        restBlogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBlog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedBlog))
            )
            .andExpect(status().isOk());

        // Validate the Blog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBlogToMatchAllProperties(updatedBlog);
    }

    @Test
    @Transactional
    void putNonExistingBlog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blog.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlogMockMvc
            .perform(put(ENTITY_API_URL_ID, blog.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(blog)))
            .andExpect(status().isBadRequest());

        // Validate the Blog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBlog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(blog))
            )
            .andExpect(status().isBadRequest());

        // Validate the Blog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBlog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(blog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Blog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBlogWithPatch() throws Exception {
        // Initialize the database
        insertedBlog = blogRepository.saveAndFlush(blog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the blog using partial update
        Blog partialUpdatedBlog = new Blog();
        partialUpdatedBlog.setId(blog.getId());

        restBlogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBlog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBlog))
            )
            .andExpect(status().isOk());

        // Validate the Blog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBlogUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBlog, blog), getPersistedBlog(blog));
    }

    @Test
    @Transactional
    void fullUpdateBlogWithPatch() throws Exception {
        // Initialize the database
        insertedBlog = blogRepository.saveAndFlush(blog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the blog using partial update
        Blog partialUpdatedBlog = new Blog();
        partialUpdatedBlog.setId(blog.getId());

        partialUpdatedBlog.content(UPDATED_CONTENT).dateTime(UPDATED_DATE_TIME).vote(UPDATED_VOTE);

        restBlogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBlog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBlog))
            )
            .andExpect(status().isOk());

        // Validate the Blog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBlogUpdatableFieldsEquals(partialUpdatedBlog, getPersistedBlog(partialUpdatedBlog));
    }

    @Test
    @Transactional
    void patchNonExistingBlog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blog.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlogMockMvc
            .perform(patch(ENTITY_API_URL_ID, blog.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(blog)))
            .andExpect(status().isBadRequest());

        // Validate the Blog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBlog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(blog))
            )
            .andExpect(status().isBadRequest());

        // Validate the Blog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBlog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(blog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Blog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBlog() throws Exception {
        // Initialize the database
        insertedBlog = blogRepository.saveAndFlush(blog);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the blog
        restBlogMockMvc
            .perform(delete(ENTITY_API_URL_ID, blog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return blogRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Blog getPersistedBlog(Blog blog) {
        return blogRepository.findById(blog.getId()).orElseThrow();
    }

    protected void assertPersistedBlogToMatchAllProperties(Blog expectedBlog) {
        assertBlogAllPropertiesEquals(expectedBlog, getPersistedBlog(expectedBlog));
    }

    protected void assertPersistedBlogToMatchUpdatableProperties(Blog expectedBlog) {
        assertBlogAllUpdatablePropertiesEquals(expectedBlog, getPersistedBlog(expectedBlog));
    }
}
