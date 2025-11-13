package ar.edu.um.programacion2.marcosibarra.service;

import ar.edu.um.programacion2.marcosibarra.domain.Sesion;
import ar.edu.um.programacion2.marcosibarra.repository.SesionRepository;
import ar.edu.um.programacion2.marcosibarra.service.dto.SesionDTO;
import ar.edu.um.programacion2.marcosibarra.service.mapper.SesionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.programacion2.marcosibarra.domain.Sesion}.
 */
@Service
@Transactional
public class SesionService {

    private static final Logger LOG = LoggerFactory.getLogger(SesionService.class);

    private final SesionRepository sesionRepository;

    private final SesionMapper sesionMapper;

    public SesionService(SesionRepository sesionRepository, SesionMapper sesionMapper) {
        this.sesionRepository = sesionRepository;
        this.sesionMapper = sesionMapper;
    }

    /**
     * Save a sesion.
     *
     * @param sesionDTO the entity to save.
     * @return the persisted entity.
     */
    public SesionDTO save(SesionDTO sesionDTO) {
        LOG.debug("Request to save Sesion : {}", sesionDTO);
        Sesion sesion = sesionMapper.toEntity(sesionDTO);
        sesion = sesionRepository.save(sesion);
        return sesionMapper.toDto(sesion);
    }

    /**
     * Update a sesion.
     *
     * @param sesionDTO the entity to save.
     * @return the persisted entity.
     */
    public SesionDTO update(SesionDTO sesionDTO) {
        LOG.debug("Request to update Sesion : {}", sesionDTO);
        Sesion sesion = sesionMapper.toEntity(sesionDTO);
        sesion = sesionRepository.save(sesion);
        return sesionMapper.toDto(sesion);
    }

    /**
     * Partially update a sesion.
     *
     * @param sesionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SesionDTO> partialUpdate(SesionDTO sesionDTO) {
        LOG.debug("Request to partially update Sesion : {}", sesionDTO);

        return sesionRepository
            .findById(sesionDTO.getId())
            .map(existingSesion -> {
                sesionMapper.partialUpdate(existingSesion, sesionDTO);

                return existingSesion;
            })
            .map(sesionRepository::save)
            .map(sesionMapper::toDto);
    }

    /**
     * Get all the sesions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SesionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Sesions");
        return sesionRepository.findAll(pageable).map(sesionMapper::toDto);
    }

    /**
     * Get all the sesions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SesionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return sesionRepository.findAllWithEagerRelationships(pageable).map(sesionMapper::toDto);
    }

    /**
     * Get one sesion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SesionDTO> findOne(Long id) {
        LOG.debug("Request to get Sesion : {}", id);
        return sesionRepository.findOneWithEagerRelationships(id).map(sesionMapper::toDto);
    }

    /**
     * Delete the sesion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Sesion : {}", id);
        sesionRepository.deleteById(id);
    }
}
