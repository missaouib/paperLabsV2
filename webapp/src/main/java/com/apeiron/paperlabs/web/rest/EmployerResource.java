package com.apeiron.paperlabs.web.rest;

import com.apeiron.paperlabs.domain.Employer;
import com.apeiron.paperlabs.repository.EmployerRepository;
import com.apeiron.paperlabs.repository.search.EmployerSearchRepository;
import com.apeiron.paperlabs.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing {@link com.apeiron.paperlabs.domain.Employer}.
 */
@RestController
@RequestMapping("/api")
public class EmployerResource {

    private final Logger log = LoggerFactory.getLogger(EmployerResource.class);

    private static final String ENTITY_NAME = "employer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployerRepository employerRepository;

    private final EmployerSearchRepository employerSearchRepository;

    public EmployerResource(EmployerRepository employerRepository, EmployerSearchRepository employerSearchRepository) {
        this.employerRepository = employerRepository;
        this.employerSearchRepository = employerSearchRepository;
    }

    /**
     * {@code POST  /employers} : Create a new employer.
     *
     * @param employer the employer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employer, or with status {@code 400 (Bad Request)} if the employer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/employers")
    public ResponseEntity<Employer> createEmployer(@RequestBody Employer employer) throws URISyntaxException {
        log.debug("REST request to save Employer : {}", employer);
        if (employer.getId() != null) {
            throw new BadRequestAlertException("A new employer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Employer result = employerRepository.save(employer);
        employerSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/employers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /employers} : Updates an existing employer.
     *
     * @param employer the employer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employer,
     * or with status {@code 400 (Bad Request)} if the employer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/employers")
    public ResponseEntity<Employer> updateEmployer(@RequestBody Employer employer) throws URISyntaxException {
        log.debug("REST request to update Employer : {}", employer);
        if (employer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Employer result = employerRepository.save(employer);
        employerSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employer.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /employers} : get all the employers.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employers in body.
     */
    @GetMapping("/employers")
    public List<Employer> getAllEmployers() {
        log.debug("REST request to get all Employers");
        return employerRepository.findAll();
    }

    /**
     * {@code GET  /employers/:id} : get the "id" employer.
     *
     * @param id the id of the employer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/employers/{id}")
    public ResponseEntity<Employer> getEmployer(@PathVariable String id) {
        log.debug("REST request to get Employer : {}", id);
        Optional<Employer> employer = employerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(employer);
    }

    /**
     * {@code DELETE  /employers/:id} : delete the "id" employer.
     *
     * @param id the id of the employer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/employers/{id}")
    public ResponseEntity<Void> deleteEmployer(@PathVariable String id) {
        log.debug("REST request to delete Employer : {}", id);
        employerRepository.deleteById(id);
        employerSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/employers?query=:query} : search for the employer corresponding
     * to the query.
     *
     * @param query the query of the employer search.
     * @return the result of the search.
     */
    @GetMapping("/_search/employers")
    public List<Employer> searchEmployers(@RequestParam String query) {
        log.debug("REST request to search Employers for query {}", query);
        return StreamSupport
            .stream(employerSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    /**
     * get all the employers by userId.
     *
     * @param userId the userId.
     * @return List of Employers.
     */
    @GetMapping("/employers/user/{userId}")
    public ResponseEntity<List<Employer>> searchEmployeesByUserId(@PathVariable String userId, @RequestParam Optional<Integer> pageNumber, @RequestParam Optional<Integer> itemsPerPage) {
        log.debug("REST request to get all Employers by user {}",userId);
        if(pageNumber.isPresent() && itemsPerPage.isPresent()) {
            Pageable pageable = PageRequest.of(pageNumber.get(), itemsPerPage.get());
            Page<Employer> page = employerRepository.findAllByUserIdEquals(userId, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
        return ResponseEntity.ok().body(employerRepository.findAllByUserIdEquals(userId));
    }

}
