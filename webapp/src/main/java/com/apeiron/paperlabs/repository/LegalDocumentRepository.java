package com.apeiron.paperlabs.repository;
import com.apeiron.paperlabs.domain.LegalDocument;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the LegalDocument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LegalDocumentRepository extends MongoRepository<LegalDocument, String> {

}
