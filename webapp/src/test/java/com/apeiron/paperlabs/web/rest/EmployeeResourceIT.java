package com.apeiron.paperlabs.web.rest;

import com.apeiron.paperlabs.PaperlabsApp;
import com.apeiron.paperlabs.domain.Employee;
import com.apeiron.paperlabs.repository.EmployeeRepository;
import com.apeiron.paperlabs.repository.search.EmployeeSearchRepository;
import com.apeiron.paperlabs.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static com.apeiron.paperlabs.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link EmployeeResource} REST controller.
 */
@SpringBootTest(classes = PaperlabsApp.class)
public class EmployeeResourceIT {

    private static final String DEFAULT_EMPLOYEE_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEE_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMPLOYEE_CIN_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEE_CIN_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EMPLOYEE_CIN_DELIVERED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EMPLOYEE_CIN_DELIVERED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_EMPLOYEE_CIN_DELIVERED_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_EMPLOYEE_CIN_DELIVERED_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEE_CIN_DELIVERED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_EMPLOYEE_FULL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEE_FULL_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_EMPLOYEE_POSTION = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEE_POSTION = "BBBBBBBBBB";

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * This repository is mocked in the com.apeiron.paperlabs.repository.search test package.
     *
     * @see com.apeiron.paperlabs.repository.search.EmployeeSearchRepositoryMockConfiguration
     */
    @Autowired
    private EmployeeSearchRepository mockEmployeeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restEmployeeMockMvc;

    private Employee employee;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmployeeResource employeeResource = new EmployeeResource(employeeRepository, mockEmployeeSearchRepository);
        this.restEmployeeMockMvc = MockMvcBuilders.standaloneSetup(employeeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createEntity() {
        Employee employee = new Employee()
            .employeeFullName(DEFAULT_EMPLOYEE_FULL_NAME)
            .employeeCinNumber(DEFAULT_EMPLOYEE_CIN_NUMBER)
            .employeeCinDeliveredDate(DEFAULT_EMPLOYEE_CIN_DELIVERED_DATE)
            .employeeCinDeliveredLocation(DEFAULT_EMPLOYEE_CIN_DELIVERED_LOCATION)
            .employeeFullAddress(DEFAULT_EMPLOYEE_FULL_ADDRESS)
            .employeePostion(DEFAULT_EMPLOYEE_POSTION);
        return employee;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createUpdatedEntity() {
        Employee employee = new Employee()
            .employeeFullName(UPDATED_EMPLOYEE_FULL_NAME)
            .employeeCinNumber(UPDATED_EMPLOYEE_CIN_NUMBER)
            .employeeCinDeliveredDate(UPDATED_EMPLOYEE_CIN_DELIVERED_DATE)
            .employeeCinDeliveredLocation(UPDATED_EMPLOYEE_CIN_DELIVERED_LOCATION)
            .employeeFullAddress(UPDATED_EMPLOYEE_FULL_ADDRESS)
            .employeePostion(UPDATED_EMPLOYEE_POSTION);
        return employee;
    }

    @BeforeEach
    public void initTest() {
        employeeRepository.deleteAll();
        employee = createEntity();
    }

    @Test
    public void createEmployee() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().size();

        // Create the Employee
        restEmployeeMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isCreated());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate + 1);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmployeeFullName()).isEqualTo(DEFAULT_EMPLOYEE_FULL_NAME);
        assertThat(testEmployee.getEmployeeCinNumber()).isEqualTo(DEFAULT_EMPLOYEE_CIN_NUMBER);
        assertThat(testEmployee.getEmployeeCinDeliveredDate()).isEqualTo(DEFAULT_EMPLOYEE_CIN_DELIVERED_DATE);
        assertThat(testEmployee.getEmployeeCinDeliveredLocation()).isEqualTo(DEFAULT_EMPLOYEE_CIN_DELIVERED_LOCATION);
        assertThat(testEmployee.getEmployeeFullAddress()).isEqualTo(DEFAULT_EMPLOYEE_FULL_ADDRESS);
        assertThat(testEmployee.getEmployeePostion()).isEqualTo(DEFAULT_EMPLOYEE_POSTION);

        // Validate the Employee in Elasticsearch
        verify(mockEmployeeSearchRepository, times(1)).save(testEmployee);
    }

    @Test
    public void createEmployeeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().size();

        // Create the Employee with an existing ID
        employee.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Employee in Elasticsearch
        verify(mockEmployeeSearchRepository, times(0)).save(employee);
    }


    @Test
    public void getAllEmployees() throws Exception {
        // Initialize the database
        employeeRepository.save(employee);

        // Get all the employeeList
        restEmployeeMockMvc.perform(get("/api/employees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId())))
            .andExpect(jsonPath("$.[*].employeeFullName").value(hasItem(DEFAULT_EMPLOYEE_FULL_NAME.toString())))
            .andExpect(jsonPath("$.[*].employeeCinNumber").value(hasItem(DEFAULT_EMPLOYEE_CIN_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].employeeCinDeliveredDate").value(hasItem(DEFAULT_EMPLOYEE_CIN_DELIVERED_DATE.toString())))
            .andExpect(jsonPath("$.[*].employeeCinDeliveredLocation").value(hasItem(DEFAULT_EMPLOYEE_CIN_DELIVERED_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].employeeFullAddress").value(hasItem(DEFAULT_EMPLOYEE_FULL_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].employeePostion").value(hasItem(DEFAULT_EMPLOYEE_POSTION.toString())));
    }
    
    @Test
    public void getEmployee() throws Exception {
        // Initialize the database
        employeeRepository.save(employee);

        // Get the employee
        restEmployeeMockMvc.perform(get("/api/employees/{id}", employee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(employee.getId()))
            .andExpect(jsonPath("$.employeeFullName").value(DEFAULT_EMPLOYEE_FULL_NAME.toString()))
            .andExpect(jsonPath("$.employeeCinNumber").value(DEFAULT_EMPLOYEE_CIN_NUMBER.toString()))
            .andExpect(jsonPath("$.employeeCinDeliveredDate").value(DEFAULT_EMPLOYEE_CIN_DELIVERED_DATE.toString()))
            .andExpect(jsonPath("$.employeeCinDeliveredLocation").value(DEFAULT_EMPLOYEE_CIN_DELIVERED_LOCATION.toString()))
            .andExpect(jsonPath("$.employeeFullAddress").value(DEFAULT_EMPLOYEE_FULL_ADDRESS.toString()))
            .andExpect(jsonPath("$.employeePostion").value(DEFAULT_EMPLOYEE_POSTION.toString()));
    }

    @Test
    public void getNonExistingEmployee() throws Exception {
        // Get the employee
        restEmployeeMockMvc.perform(get("/api/employees/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateEmployee() throws Exception {
        // Initialize the database
        employeeRepository.save(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee
        Employee updatedEmployee = employeeRepository.findById(employee.getId()).get();
        updatedEmployee
            .employeeFullName(UPDATED_EMPLOYEE_FULL_NAME)
            .employeeCinNumber(UPDATED_EMPLOYEE_CIN_NUMBER)
            .employeeCinDeliveredDate(UPDATED_EMPLOYEE_CIN_DELIVERED_DATE)
            .employeeCinDeliveredLocation(UPDATED_EMPLOYEE_CIN_DELIVERED_LOCATION)
            .employeeFullAddress(UPDATED_EMPLOYEE_FULL_ADDRESS)
            .employeePostion(UPDATED_EMPLOYEE_POSTION);

        restEmployeeMockMvc.perform(put("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEmployee)))
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmployeeFullName()).isEqualTo(UPDATED_EMPLOYEE_FULL_NAME);
        assertThat(testEmployee.getEmployeeCinNumber()).isEqualTo(UPDATED_EMPLOYEE_CIN_NUMBER);
        assertThat(testEmployee.getEmployeeCinDeliveredDate()).isEqualTo(UPDATED_EMPLOYEE_CIN_DELIVERED_DATE);
        assertThat(testEmployee.getEmployeeCinDeliveredLocation()).isEqualTo(UPDATED_EMPLOYEE_CIN_DELIVERED_LOCATION);
        assertThat(testEmployee.getEmployeeFullAddress()).isEqualTo(UPDATED_EMPLOYEE_FULL_ADDRESS);
        assertThat(testEmployee.getEmployeePostion()).isEqualTo(UPDATED_EMPLOYEE_POSTION);

        // Validate the Employee in Elasticsearch
        verify(mockEmployeeSearchRepository, times(1)).save(testEmployee);
    }

    @Test
    public void updateNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Create the Employee

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc.perform(put("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Employee in Elasticsearch
        verify(mockEmployeeSearchRepository, times(0)).save(employee);
    }

    @Test
    public void deleteEmployee() throws Exception {
        // Initialize the database
        employeeRepository.save(employee);

        int databaseSizeBeforeDelete = employeeRepository.findAll().size();

        // Delete the employee
        restEmployeeMockMvc.perform(delete("/api/employees/{id}", employee.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Employee in Elasticsearch
        verify(mockEmployeeSearchRepository, times(1)).deleteById(employee.getId());
    }

    @Test
    public void searchEmployee() throws Exception {
        // Initialize the database
        employeeRepository.save(employee);
        when(mockEmployeeSearchRepository.search(queryStringQuery("id:" + employee.getId())))
            .thenReturn(Collections.singletonList(employee));
        // Search the employee
        restEmployeeMockMvc.perform(get("/api/_search/employees?query=id:" + employee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId())))
            .andExpect(jsonPath("$.[*].employeeFullName").value(hasItem(DEFAULT_EMPLOYEE_FULL_NAME)))
            .andExpect(jsonPath("$.[*].employeeCinNumber").value(hasItem(DEFAULT_EMPLOYEE_CIN_NUMBER)))
            .andExpect(jsonPath("$.[*].employeeCinDeliveredDate").value(hasItem(DEFAULT_EMPLOYEE_CIN_DELIVERED_DATE.toString())))
            .andExpect(jsonPath("$.[*].employeeCinDeliveredLocation").value(hasItem(DEFAULT_EMPLOYEE_CIN_DELIVERED_LOCATION)))
            .andExpect(jsonPath("$.[*].employeeFullAddress").value(hasItem(DEFAULT_EMPLOYEE_FULL_ADDRESS)))
            .andExpect(jsonPath("$.[*].employeePostion").value(hasItem(DEFAULT_EMPLOYEE_POSTION)));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employee.class);
        Employee employee1 = new Employee();
        employee1.setId("id1");
        Employee employee2 = new Employee();
        employee2.setId(employee1.getId());
        assertThat(employee1).isEqualTo(employee2);
        employee2.setId("id2");
        assertThat(employee1).isNotEqualTo(employee2);
        employee1.setId(null);
        assertThat(employee1).isNotEqualTo(employee2);
    }
}
