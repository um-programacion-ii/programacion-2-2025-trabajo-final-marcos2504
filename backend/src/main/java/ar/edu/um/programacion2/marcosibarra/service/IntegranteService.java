package ar.edu.um.programacion2.marcosibarra.service;

import ar.edu.um.programacion2.marcosibarra.domain.Integrante;
import ar.edu.um.programacion2.marcosibarra.repository.IntegranteRepository;
import ar.edu.um.programacion2.marcosibarra.service.dto.IntegranteDTO;
import ar.edu.um.programacion2.marcosibarra.service.mapper.IntegranteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.programacion2.marcosibarra.domain.Integrante}.
 */
@Service
@Transactional
public class IntegranteService {

    private static final Logger LOG = LoggerFactory.getLogger(IntegranteService.class);

    private final IntegranteRepository integranteRepository;

    private final IntegranteMapper integranteMapper;

    public IntegranteService(IntegranteRepository integranteRepository, IntegranteMapper integranteMapper) {
        this.integranteRepository = integranteRepository;
        this.integranteMapper = integranteMapper;
    }

    /**
     * Save a integrante.
     *
     * @param integranteDTO the entity to save.
     * @return the persisted entity.
     */
    public IntegranteDTO save(IntegranteDTO integranteDTO) {
        LOG.debug("Request to save Integrante : {}", integranteDTO);
        Integrante integrante = integranteMapper.toEntity(integranteDTO);
        integrante = integranteRepository.save(integrante);
        return integranteMapper.toDto(integrante);
    }

    /**
     * Update a integrante.
     *
     * @param integranteDTO the entity to save.
     * @return the persisted entity.
     */
    public IntegranteDTO update(IntegranteDTO integranteDTO) {
        LOG.debug("Request to update Integrante : {}", integranteDTO);
        Integrante integrante = integranteMapper.toEntity(integranteDTO);
        integrante = integranteRepository.save(integrante);
        return integranteMapper.toDto(integrante);
    }

    /**
     * Partially update a integrante.
     *
     * @param integranteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IntegranteDTO> partialUpdate(IntegranteDTO integranteDTO) {
        LOG.debug("Request to partially update Integrante : {}", integranteDTO);

        return integranteRepository
            .findById(integranteDTO.getId())
            .map(existingIntegrante -> {
                integranteMapper.partialUpdate(existingIntegrante, integranteDTO);

                return existingIntegrante;
            })
            .map(integranteRepository::save)
            .map(integranteMapper::toDto);
    }

    /**
     * Get all the integrantes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<IntegranteDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Integrantes");
        return integranteRepository.findAll(pageable).map(integranteMapper::toDto);
    }

    /**
     * Get one integrante by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IntegranteDTO> findOne(Long id) {
        LOG.debug("Request to get Integrante : {}", id);
        return integranteRepository.findById(id).map(integranteMapper::toDto);
    }

    /**
     * Delete the integrante by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Integrante : {}", id);
        integranteRepository.deleteById(id);
    }
}
