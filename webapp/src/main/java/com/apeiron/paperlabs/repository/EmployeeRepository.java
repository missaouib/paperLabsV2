package com.apeiron.paperlabs.repository;
import com.apeiron.paperlabs.domain.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data MongoDB repository for the Employee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {
    List<Employee> findAllByUserIdEquals(String userId);
    Optional<Employee> findByEmployeeCinNumberEquals(String cin);
}
