package com.apeiron.paperlabs.service;

import com.apeiron.paperlabs.config.Constants;
import com.apeiron.paperlabs.domain.Authority;
import com.apeiron.paperlabs.domain.Company;
import com.apeiron.paperlabs.domain.Employee;
import com.apeiron.paperlabs.domain.User;
import com.apeiron.paperlabs.repository.AuthorityRepository;
import com.apeiron.paperlabs.repository.CompanyRepository;
import com.apeiron.paperlabs.repository.EmployeeRepository;
import com.apeiron.paperlabs.repository.UserRepository;
import com.apeiron.paperlabs.repository.search.UserSearchRepository;
import com.apeiron.paperlabs.security.AuthoritiesConstants;
import com.apeiron.paperlabs.security.SecurityUtils;
import com.apeiron.paperlabs.service.dto.UserDTO;
import com.apeiron.paperlabs.service.mapper.UserMapper;
import com.apeiron.paperlabs.service.util.RandomUtil;

import org.elasticsearch.index.query.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.util.CloseableIterator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service class for managing users.
 */
@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserSearchRepository userSearchRepository;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    private final CompanyRepository companyRepository;

    private final EmployeeRepository employeeRepository;

    private final UserMapper userMapper;

    private final ElasticsearchOperations elasticsearchOperations;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserSearchRepository userSearchRepository, AuthorityRepository authorityRepository, CacheManager cacheManager, CompanyRepository companyRepository, EmployeeRepository employeeRepository, UserMapper userMapper, ElasticsearchOperations elasticsearchOperations) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userSearchRepository = userSearchRepository;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
        this.userMapper = userMapper;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                userRepository.save(user);
                userSearchRepository.save(user);
                this.clearUserCaches(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository.findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                userRepository.save(user);
                this.clearUserCaches(user);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmailIgnoreCase(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                userRepository.save(user);
                this.clearUserCaches(user);
                return user;
            });
    }

    public User registerUser(UserDTO userDTO, String password) {
        userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new EmailAlreadyUsedException();
            }
        });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getEmail().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setEmail(userDTO.getEmail().toLowerCase());
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        userSearchRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(User existingUser){
        if (existingUser.getActivated()) {
             return false;
        }
        userRepository.delete(existingUser);
        this.clearUserCaches(existingUser);
        return true;
    }

    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getEmail().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail().toLowerCase());
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO.getAuthorities().stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        userSearchRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email.toLowerCase());
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                user.setLogin(email.toLowerCase());
                userRepository.save(user);
                userSearchRepository.save(user);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
            });
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
            .findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                this.clearUserCaches(user);
                user.setLogin(userDTO.getEmail().toLowerCase());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                user.setEmail(userDTO.getEmail().toLowerCase());
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                userRepository.save(user);
                userSearchRepository.save(user);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
    }

    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            userRepository.delete(user);
            userSearchRepository.delete(user);
            this.clearUserCaches(user);
            log.debug("Deleted User: {}", user);
        });
    }

    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                userRepository.save(user);
                this.clearUserCaches(user);
                log.debug("Changed password for User: {}", user);
            });
    }

    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
    }

    public Optional<UserDTO> getUserWithAuthoritiesByLogin(String login){

        Optional<UserDTO> optionalUser = userRepository.findOneByLogin(login).map(UserDTO::new);

        if (optionalUser.isPresent()) {
            List<Company> companyList = companyRepository.getAllByUserIdEquals(optionalUser.get().getId());
            List<Employee> employeeList = employeeRepository.findAllByUserIdEquals(optionalUser.get().getId());
            if (!companyList.isEmpty()) {
                optionalUser.get().setListCompanies(companyList);
            }
            if (!employeeList.isEmpty()) {
                optionalUser.get().setListEmployees(employeeList);
            }

        }
        return optionalUser;
    }


    public Optional<User> getUserWithAuthorities(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(user -> {
                log.debug("Deleting not activated user {}", user.getLogin());
                userRepository.delete(user);
                userSearchRepository.delete(user);
                this.clearUserCaches(user);
            });
    }

    /**
     * Gets a list of all the authorities.
     * @return a list of all the authorities.
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }


    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
    }

    /**
     * Search for the user first Name, last Name, and login corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    public Page<UserDTO> searchUsersWithFields(String query, Pageable pageable) {
        log.debug("Request to search for a page of Users for query {}", query);

        QueryBuilder queryBuilder = boolQuery().should(
            queryStringQuery("*"+query+"*").analyzeWildcard(true)
                .field("firstName")
                .field("lastName")
                .field("login")
        );

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
            .withFilter(queryBuilder)
            .withIndices("user")
            .withPageable(PageRequest.of(0, 100))
            .build();

        CloseableIterator<User> stream = elasticsearchOperations.stream(searchQuery, User.class);

        List<UserDTO> legalDocumentList = new ArrayList<>();
        while (stream.hasNext()) {
            legalDocumentList.add(userMapper.userToUserDTO(stream.next()));
        }

        return new PageImpl<>(legalDocumentList,
            new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()),
            legalDocumentList.size());

    }
}
