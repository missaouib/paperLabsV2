package com.apeiron.paperlabs.repository;

import com.apeiron.paperlabs.domain.Employer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data MongoDB repository for the Employer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployerRepository extends MongoRepository<Employer, String> {
    List<Employer> findAllByUserIdEquals(String userId);
    Page<Employer> findAllByUserIdEquals(String userId, Pageable pageable);
    Optional<Employer> findByEmployerCinNumberEquals(Long cin);
}
