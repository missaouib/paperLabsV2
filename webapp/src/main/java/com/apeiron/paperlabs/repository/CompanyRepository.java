package com.apeiron.paperlabs.repository;

import com.apeiron.paperlabs.domain.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data MongoDB repository for the Company entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyRepository extends MongoRepository<Company, String> {

    List<Company> getAllByUserIdEquals(String userId);
    Page<Company> getAllByUserIdEquals(String userId, Pageable pageable);
    Optional<Company> findByCompanyNameEquals(String companyName);
}
