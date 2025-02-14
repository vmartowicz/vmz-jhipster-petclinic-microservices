package fr.vmz.jhipster.petclinic.customer.service;

import fr.vmz.jhipster.petclinic.customer.domain.Owner;
import fr.vmz.jhipster.petclinic.customer.repository.OwnerRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.vmz.jhipster.petclinic.customer.domain.Owner}.
 */
@Service
@Transactional
public class OwnerService {

    private static final Logger LOG = LoggerFactory.getLogger(OwnerService.class);

    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    /**
     * Save a owner.
     *
     * @param owner the entity to save.
     * @return the persisted entity.
     */
    public Owner save(Owner owner) {
        LOG.debug("Request to save Owner : {}", owner);
        return ownerRepository.save(owner);
    }

    /**
     * Update a owner.
     *
     * @param owner the entity to save.
     * @return the persisted entity.
     */
    public Owner update(Owner owner) {
        LOG.debug("Request to update Owner : {}", owner);
        return ownerRepository.save(owner);
    }

    /**
     * Partially update a owner.
     *
     * @param owner the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Owner> partialUpdate(Owner owner) {
        LOG.debug("Request to partially update Owner : {}", owner);

        return ownerRepository
            .findById(owner.getId())
            .map(existingOwner -> {
                if (owner.getFirstName() != null) {
                    existingOwner.setFirstName(owner.getFirstName());
                }
                if (owner.getLastName() != null) {
                    existingOwner.setLastName(owner.getLastName());
                }
                if (owner.getAddress() != null) {
                    existingOwner.setAddress(owner.getAddress());
                }
                if (owner.getCity() != null) {
                    existingOwner.setCity(owner.getCity());
                }
                if (owner.getTelephone() != null) {
                    existingOwner.setTelephone(owner.getTelephone());
                }

                return existingOwner;
            })
            .map(ownerRepository::save);
    }

    /**
     * Get all the owners.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Owner> findAll(Pageable pageable) {
        LOG.debug("Request to get all Owners");
        return ownerRepository.findAll(pageable);
    }

    /**
     * Get one owner by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Owner> findOne(Long id) {
        LOG.debug("Request to get Owner : {}", id);
        return ownerRepository.findById(id);
    }

    /**
     * Delete the owner by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Owner : {}", id);
        ownerRepository.deleteById(id);
    }
}
