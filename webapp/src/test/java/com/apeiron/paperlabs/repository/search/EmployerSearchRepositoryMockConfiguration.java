package com.apeiron.paperlabs.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link EmployerSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class EmployerSearchRepositoryMockConfiguration {

    @MockBean
    private EmployerSearchRepository mockEmployerSearchRepository;

}
