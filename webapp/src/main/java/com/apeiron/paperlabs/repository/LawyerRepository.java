package com.apeiron.paperlabs.repository;
import com.apeiron.paperlabs.domain.Lawyer;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Lawyer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LawyerRepository extends MongoRepository<Lawyer, String> {

}
