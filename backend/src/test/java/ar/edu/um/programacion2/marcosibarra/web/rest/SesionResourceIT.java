package ar.edu.um.programacion2.marcosibarra.web.rest;

import static ar.edu.um.programacion2.marcosibarra.domain.SesionAsserts.*;
import static ar.edu.um.programacion2.marcosibarra.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.programacion2.marcosibarra.IntegrationTest;
import ar.edu.um.programacion2.marcosibarra.domain.Sesion;
import ar.edu.um.programacion2.marcosibarra.repository.SesionRepository;
import ar.edu.um.programacion2.marcosibarra.repository.UserRepository;
import ar.edu.um.programacion2.marcosibarra.service.SesionService;
import ar.edu.um.programacion2.marcosibarra.service.dto.SesionDTO;
import ar.edu.um.programacion2.marcosibarra.service.mapper.SesionMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link SesionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SesionResourceIT {

    private static final String DEFAULT_TOKEN_JWT = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN_JWT = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_EXPIRACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_EXPIRACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ACTIVA = false;
    private static final Boolean UPDATED_ACTIVA = true;

    private static final Instant DEFAULT_ULTIMO_ACCESO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ULTIMO_ACCESO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/sesions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SesionRepository sesionRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private SesionRepository sesionRepositoryMock;

    @Autowired
    private SesionMapper sesionMapper;

    @Mock
    private SesionService sesionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSesionMockMvc;

    private Sesion sesion;

    private Sesion insertedSesion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sesion createEntity() {
        return new Sesion()
            .tokenJWT(DEFAULT_TOKEN_JWT)
            .fechaInicio(DEFAULT_FECHA_INICIO)
            .fechaExpiracion(DEFAULT_FECHA_EXPIRACION)
            .activa(DEFAULT_ACTIVA)
            .ultimoAcceso(DEFAULT_ULTIMO_ACCESO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sesion createUpdatedEntity() {
        return new Sesion()
            .tokenJWT(UPDATED_TOKEN_JWT)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaExpiracion(UPDATED_FECHA_EXPIRACION)
            .activa(UPDATED_ACTIVA)
            .ultimoAcceso(UPDATED_ULTIMO_ACCESO);
    }

    @BeforeEach
    void initTest() {
        sesion = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSesion != null) {
            sesionRepository.delete(insertedSesion);
            insertedSesion = null;
        }
    }

    @Test
    @Transactional
    void createSesion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Sesion
        SesionDTO sesionDTO = sesionMapper.toDto(sesion);
        var returnedSesionDTO = om.readValue(
            restSesionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sesionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SesionDTO.class
        );

        // Validate the Sesion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSesion = sesionMapper.toEntity(returnedSesionDTO);
        assertSesionUpdatableFieldsEquals(returnedSesion, getPersistedSesion(returnedSesion));

        insertedSesion = returnedSesion;
    }

    @Test
    @Transactional
    void createSesionWithExistingId() throws Exception {
        // Create the Sesion with an existing ID
        sesion.setId(1L);
        SesionDTO sesionDTO = sesionMapper.toDto(sesion);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSesionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sesionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sesion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaInicioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sesion.setFechaInicio(null);

        // Create the Sesion, which fails.
        SesionDTO sesionDTO = sesionMapper.toDto(sesion);

        restSesionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sesionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sesion.setActiva(null);

        // Create the Sesion, which fails.
        SesionDTO sesionDTO = sesionMapper.toDto(sesion);

        restSesionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sesionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSesions() throws Exception {
        // Initialize the database
        insertedSesion = sesionRepository.saveAndFlush(sesion);

        // Get all the sesionList
        restSesionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sesion.getId().intValue())))
            .andExpect(jsonPath("$.[*].tokenJWT").value(hasItem(DEFAULT_TOKEN_JWT)))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaExpiracion").value(hasItem(DEFAULT_FECHA_EXPIRACION.toString())))
            .andExpect(jsonPath("$.[*].activa").value(hasItem(DEFAULT_ACTIVA)))
            .andExpect(jsonPath("$.[*].ultimoAcceso").value(hasItem(DEFAULT_ULTIMO_ACCESO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSesionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(sesionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSesionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(sesionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSesionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(sesionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSesionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(sesionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSesion() throws Exception {
        // Initialize the database
        insertedSesion = sesionRepository.saveAndFlush(sesion);

        // Get the sesion
        restSesionMockMvc
            .perform(get(ENTITY_API_URL_ID, sesion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sesion.getId().intValue()))
            .andExpect(jsonPath("$.tokenJWT").value(DEFAULT_TOKEN_JWT))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaExpiracion").value(DEFAULT_FECHA_EXPIRACION.toString()))
            .andExpect(jsonPath("$.activa").value(DEFAULT_ACTIVA))
            .andExpect(jsonPath("$.ultimoAcceso").value(DEFAULT_ULTIMO_ACCESO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSesion() throws Exception {
        // Get the sesion
        restSesionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSesion() throws Exception {
        // Initialize the database
        insertedSesion = sesionRepository.saveAndFlush(sesion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sesion
        Sesion updatedSesion = sesionRepository.findById(sesion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSesion are not directly saved in db
        em.detach(updatedSesion);
        updatedSesion
            .tokenJWT(UPDATED_TOKEN_JWT)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaExpiracion(UPDATED_FECHA_EXPIRACION)
            .activa(UPDATED_ACTIVA)
            .ultimoAcceso(UPDATED_ULTIMO_ACCESO);
        SesionDTO sesionDTO = sesionMapper.toDto(updatedSesion);

        restSesionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sesionDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sesionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Sesion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSesionToMatchAllProperties(updatedSesion);
    }

    @Test
    @Transactional
    void putNonExistingSesion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sesion.setId(longCount.incrementAndGet());

        // Create the Sesion
        SesionDTO sesionDTO = sesionMapper.toDto(sesion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSesionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sesionDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sesionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sesion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSesion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sesion.setId(longCount.incrementAndGet());

        // Create the Sesion
        SesionDTO sesionDTO = sesionMapper.toDto(sesion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSesionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sesionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sesion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSesion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sesion.setId(longCount.incrementAndGet());

        // Create the Sesion
        SesionDTO sesionDTO = sesionMapper.toDto(sesion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSesionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sesionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sesion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSesionWithPatch() throws Exception {
        // Initialize the database
        insertedSesion = sesionRepository.saveAndFlush(sesion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sesion using partial update
        Sesion partialUpdatedSesion = new Sesion();
        partialUpdatedSesion.setId(sesion.getId());

        partialUpdatedSesion.tokenJWT(UPDATED_TOKEN_JWT).fechaInicio(UPDATED_FECHA_INICIO).fechaExpiracion(UPDATED_FECHA_EXPIRACION);

        restSesionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSesion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSesion))
            )
            .andExpect(status().isOk());

        // Validate the Sesion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSesionUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSesion, sesion), getPersistedSesion(sesion));
    }

    @Test
    @Transactional
    void fullUpdateSesionWithPatch() throws Exception {
        // Initialize the database
        insertedSesion = sesionRepository.saveAndFlush(sesion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sesion using partial update
        Sesion partialUpdatedSesion = new Sesion();
        partialUpdatedSesion.setId(sesion.getId());

        partialUpdatedSesion
            .tokenJWT(UPDATED_TOKEN_JWT)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaExpiracion(UPDATED_FECHA_EXPIRACION)
            .activa(UPDATED_ACTIVA)
            .ultimoAcceso(UPDATED_ULTIMO_ACCESO);

        restSesionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSesion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSesion))
            )
            .andExpect(status().isOk());

        // Validate the Sesion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSesionUpdatableFieldsEquals(partialUpdatedSesion, getPersistedSesion(partialUpdatedSesion));
    }

    @Test
    @Transactional
    void patchNonExistingSesion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sesion.setId(longCount.incrementAndGet());

        // Create the Sesion
        SesionDTO sesionDTO = sesionMapper.toDto(sesion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSesionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sesionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sesionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sesion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSesion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sesion.setId(longCount.incrementAndGet());

        // Create the Sesion
        SesionDTO sesionDTO = sesionMapper.toDto(sesion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSesionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sesionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sesion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSesion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sesion.setId(longCount.incrementAndGet());

        // Create the Sesion
        SesionDTO sesionDTO = sesionMapper.toDto(sesion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSesionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(sesionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sesion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSesion() throws Exception {
        // Initialize the database
        insertedSesion = sesionRepository.saveAndFlush(sesion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sesion
        restSesionMockMvc
            .perform(delete(ENTITY_API_URL_ID, sesion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sesionRepository.count();
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

    protected Sesion getPersistedSesion(Sesion sesion) {
        return sesionRepository.findById(sesion.getId()).orElseThrow();
    }

    protected void assertPersistedSesionToMatchAllProperties(Sesion expectedSesion) {
        assertSesionAllPropertiesEquals(expectedSesion, getPersistedSesion(expectedSesion));
    }

    protected void assertPersistedSesionToMatchUpdatableProperties(Sesion expectedSesion) {
        assertSesionAllUpdatablePropertiesEquals(expectedSesion, getPersistedSesion(expectedSesion));
    }
}
