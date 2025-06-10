package fr.vmz.jhipster.petclinic.customer.service.impl;

import fr.vmz.jhipster.petclinic.customer.domain.Owner;
import fr.vmz.jhipster.petclinic.customer.repository.OwnerRepository;
import fr.vmz.jhipster.petclinic.customer.service.OwnerService;
import fr.vmz.jhipster.petclinic.customer.service.dto.OwnerDTO;
import fr.vmz.jhipster.petclinic.customer.service.mapper.OwnerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.vmz.jhipster.petclinic.customer.domain.Owner}.
 */
@Service
@Transactional
public class OwnerServiceImpl implements OwnerService {

    private static final Logger LOG = LoggerFactory.getLogger(OwnerServiceImpl.class);

    private final OwnerRepository ownerRepository;

    private final OwnerMapper ownerMapper;

    public OwnerServiceImpl(OwnerRepository ownerRepository, OwnerMapper ownerMapper) {
        this.ownerRepository = ownerRepository;
        this.ownerMapper = ownerMapper;
    }

    @Override
    public OwnerDTO save(OwnerDTO ownerDTO) {
        LOG.debug("Request to save Owner : {}", ownerDTO);
        Owner owner = ownerMapper.toEntity(ownerDTO);
        owner = ownerRepository.save(owner);
        return ownerMapper.toDto(owner);
    }

    @Override
    public OwnerDTO update(OwnerDTO ownerDTO) {
        LOG.debug("Request to update Owner : {}", ownerDTO);
        Owner owner = ownerMapper.toEntity(ownerDTO);
        owner = ownerRepository.save(owner);
        return ownerMapper.toDto(owner);
    }

    @Override
    public Optional<OwnerDTO> partialUpdate(OwnerDTO ownerDTO) {
        LOG.debug("Request to partially update Owner : {}", ownerDTO);

        return ownerRepository
            .findById(ownerDTO.getId())
            .map(existingOwner -> {
                ownerMapper.partialUpdate(existingOwner, ownerDTO);

                return existingOwner;
            })
            .map(ownerRepository::save)
            .map(ownerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OwnerDTO> findOne(Long id) {
        LOG.debug("Request to get Owner : {}", id);
        return ownerRepository.findById(id).map(ownerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Owner : {}", id);
        ownerRepository.deleteById(id);
    }
}
