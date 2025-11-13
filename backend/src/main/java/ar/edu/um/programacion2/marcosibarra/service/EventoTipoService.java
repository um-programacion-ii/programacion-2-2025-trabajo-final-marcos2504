package ar.edu.um.programacion2.marcosibarra.service;

import ar.edu.um.programacion2.marcosibarra.domain.EventoTipo;
import ar.edu.um.programacion2.marcosibarra.repository.EventoTipoRepository;
import ar.edu.um.programacion2.marcosibarra.service.dto.EventoTipoDTO;
import ar.edu.um.programacion2.marcosibarra.service.mapper.EventoTipoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.programacion2.marcosibarra.domain.EventoTipo}.
 */
@Service
@Transactional
public class EventoTipoService {

    private static final Logger LOG = LoggerFactory.getLogger(EventoTipoService.class);

    private final EventoTipoRepository eventoTipoRepository;

    private final EventoTipoMapper eventoTipoMapper;

    public EventoTipoService(EventoTipoRepository eventoTipoRepository, EventoTipoMapper eventoTipoMapper) {
        this.eventoTipoRepository = eventoTipoRepository;
        this.eventoTipoMapper = eventoTipoMapper;
    }

    /**
     * Save a eventoTipo.
     *
     * @param eventoTipoDTO the entity to save.
     * @return the persisted entity.
     */
    public EventoTipoDTO save(EventoTipoDTO eventoTipoDTO) {
        LOG.debug("Request to save EventoTipo : {}", eventoTipoDTO);
        EventoTipo eventoTipo = eventoTipoMapper.toEntity(eventoTipoDTO);
        eventoTipo = eventoTipoRepository.save(eventoTipo);
        return eventoTipoMapper.toDto(eventoTipo);
    }

    /**
     * Update a eventoTipo.
     *
     * @param eventoTipoDTO the entity to save.
     * @return the persisted entity.
     */
    public EventoTipoDTO update(EventoTipoDTO eventoTipoDTO) {
        LOG.debug("Request to update EventoTipo : {}", eventoTipoDTO);
        EventoTipo eventoTipo = eventoTipoMapper.toEntity(eventoTipoDTO);
        eventoTipo = eventoTipoRepository.save(eventoTipo);
        return eventoTipoMapper.toDto(eventoTipo);
    }

    /**
     * Partially update a eventoTipo.
     *
     * @param eventoTipoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EventoTipoDTO> partialUpdate(EventoTipoDTO eventoTipoDTO) {
        LOG.debug("Request to partially update EventoTipo : {}", eventoTipoDTO);

        return eventoTipoRepository
            .findById(eventoTipoDTO.getId())
            .map(existingEventoTipo -> {
                eventoTipoMapper.partialUpdate(existingEventoTipo, eventoTipoDTO);

                return existingEventoTipo;
            })
            .map(eventoTipoRepository::save)
            .map(eventoTipoMapper::toDto);
    }

    /**
     * Get all the eventoTipos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EventoTipoDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all EventoTipos");
        return eventoTipoRepository.findAll(pageable).map(eventoTipoMapper::toDto);
    }

    /**
     * Get one eventoTipo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EventoTipoDTO> findOne(Long id) {
        LOG.debug("Request to get EventoTipo : {}", id);
        return eventoTipoRepository.findById(id).map(eventoTipoMapper::toDto);
    }

    /**
     * Delete the eventoTipo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EventoTipo : {}", id);
        eventoTipoRepository.deleteById(id);
    }
}
