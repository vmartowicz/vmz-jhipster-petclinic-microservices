package fr.vmz.jhipster.petclinic.vet.service.impl;

import fr.vmz.jhipster.petclinic.vet.domain.Vet;
import fr.vmz.jhipster.petclinic.vet.repository.VetRepository;
import fr.vmz.jhipster.petclinic.vet.service.VetService;
import fr.vmz.jhipster.petclinic.vet.service.dto.VetDTO;
import fr.vmz.jhipster.petclinic.vet.service.mapper.VetMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.vmz.jhipster.petclinic.vet.domain.Vet}.
 */
@Service
@Transactional
public class VetServiceImpl implements VetService {

    private static final Logger LOG = LoggerFactory.getLogger(VetServiceImpl.class);

    private final VetRepository vetRepository;

    private final VetMapper vetMapper;

    public VetServiceImpl(VetRepository vetRepository, VetMapper vetMapper) {
        this.vetRepository = vetRepository;
        this.vetMapper = vetMapper;
    }

    @Override
    public VetDTO save(VetDTO vetDTO) {
        LOG.debug("Request to save Vet : {}", vetDTO);
        Vet vet = vetMapper.toEntity(vetDTO);
        vet = vetRepository.save(vet);
        return vetMapper.toDto(vet);
    }

    @Override
    public VetDTO update(VetDTO vetDTO) {
        LOG.debug("Request to update Vet : {}", vetDTO);
        Vet vet = vetMapper.toEntity(vetDTO);
        vet = vetRepository.save(vet);
        return vetMapper.toDto(vet);
    }

    @Override
    public Optional<VetDTO> partialUpdate(VetDTO vetDTO) {
        LOG.debug("Request to partially update Vet : {}", vetDTO);

        return vetRepository
            .findById(vetDTO.getId())
            .map(existingVet -> {
                vetMapper.partialUpdate(existingVet, vetDTO);

                return existingVet;
            })
            .map(vetRepository::save)
            .map(vetMapper::toDto);
    }

    public Page<VetDTO> findAllWithEagerRelationships(Pageable pageable) {
        return vetRepository.findAllWithEagerRelationships(pageable).map(vetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VetDTO> findOne(Long id) {
        LOG.debug("Request to get Vet : {}", id);
        return vetRepository.findOneWithEagerRelationships(id).map(vetMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Vet : {}", id);
        vetRepository.deleteById(id);
    }
}
