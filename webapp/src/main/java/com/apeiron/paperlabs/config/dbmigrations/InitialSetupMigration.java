package com.apeiron.paperlabs.config.dbmigrations;

import com.apeiron.paperlabs.domain.Authority;
import com.apeiron.paperlabs.domain.User;
import com.apeiron.paperlabs.security.AuthoritiesConstants;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.Instant;

/**
 * Creates the initial database setup.
 */
@ChangeLog(order = "001")
public class InitialSetupMigration {

    @ChangeSet(order = "01", author = "initiator", id = "01-addAuthorities")
    public void addAuthorities(MongoTemplate mongoTemplate) {
        Authority adminAuthority = new Authority();
        adminAuthority.setName(AuthoritiesConstants.ADMIN);
        Authority userAuthority = new Authority();
        userAuthority.setName(AuthoritiesConstants.USER);
        mongoTemplate.save(adminAuthority);
        mongoTemplate.save(userAuthority);
    }

    @ChangeSet(order = "02", author = "initiator", id = "02-addUsers")
    public void addUsers(MongoTemplate mongoTemplate) {
        Authority adminAuthority = new Authority();
        adminAuthority.setName(AuthoritiesConstants.ADMIN);
        Authority userAuthority = new Authority();
        userAuthority.setName(AuthoritiesConstants.USER);
        
        User adminUser = new User();
        adminUser.setId("user-0");
        adminUser.setLogin("rhimiirami@gmail.com");
        adminUser.setPassword("$2a$10$1xi7b7xBie/DCiFF9u2Bme0c1e.f6jJM9i76kA0e1C4Ues0dGea2u");
        adminUser.setFirstName("Rami");
        adminUser.setLastName("Rhimi");
        adminUser.setEmail("rhimiirami@gmail.com");
        adminUser.setActivated(true);
        adminUser.setLangKey("fr");
        adminUser.setCreatedBy(adminUser.getLogin());
        adminUser.setCreatedDate(Instant.now());
        adminUser.getAuthorities().add(adminAuthority);
        adminUser.getAuthorities().add(userAuthority);
        mongoTemplate.save(adminUser);
        
        adminUser = new User();
        adminUser.setId("user-0");
        adminUser.setLogin("rhimiirami@gmail.com");
        adminUser.setPassword("$2a$10$1xi7b7xBie/DCiFF9u2Bme0c1e.f6jJM9i76kA0e1C4Ues0dGea2u");
        adminUser.setFirstName("Rami");
        adminUser.setLastName("Rhimi");
        adminUser.setEmail("rhimiirami@gmail.com");
        adminUser.setActivated(true);
        adminUser.setLangKey("fr");
        adminUser.setCreatedBy(adminUser.getLogin());
        adminUser.setCreatedDate(Instant.now());
        adminUser.getAuthorities().add(adminAuthority);
        adminUser.getAuthorities().add(userAuthority);
        mongoTemplate.save(adminUser);

        adminUser = new User();
        adminUser.setId("user-1");
        adminUser.setLogin("wael.sakhri@apeiron-tech.com");
        adminUser.setPassword("$2a$10$1xi7b7xBie/DCiFF9u2Bme0c1e.f6jJM9i76kA0e1C4Ues0dGea2u");
        adminUser.setFirstName("Wael");
        adminUser.setLastName("Sakhri");
        adminUser.setEmail("wael.sakhri@apeiron-tech.com");
        adminUser.setActivated(true);
        adminUser.setLangKey("fr");
        adminUser.setCreatedBy(adminUser.getLogin());
        adminUser.setCreatedDate(Instant.now());
        adminUser.getAuthorities().add(adminAuthority);
        adminUser.getAuthorities().add(userAuthority);
        mongoTemplate.save(adminUser);

        
        adminUser.setId("user-2");
        adminUser.setLogin("ghandour787@gmail.com");
        adminUser.setPassword("$2a$10$1xi7b7xBie/DCiFF9u2Bme0c1e.f6jJM9i76kA0e1C4Ues0dGea2u");
        adminUser.setFirstName("Abdelkrim");
        adminUser.setLastName("Ghandour");
        adminUser.setEmail("ghandour787@gmail.com");
        adminUser.setActivated(true);
        adminUser.setLangKey("fr");
        adminUser.setCreatedBy(adminUser.getLogin());
        adminUser.setCreatedDate(Instant.now());
        adminUser.getAuthorities().add(adminAuthority);
        adminUser.getAuthorities().add(userAuthority);
        mongoTemplate.save(adminUser);

        adminUser.setId("user-3");
        adminUser.setLogin("belhassen.zinelabidine@apeiron-tech.com");
        adminUser.setPassword("$2a$10$1xi7b7xBie/DCiFF9u2Bme0c1e.f6jJM9i76kA0e1C4Ues0dGea2u");
        adminUser.setFirstName("Mohamed Belhassen");
        adminUser.setLastName("Zinelabidine");
        adminUser.setEmail("belhassen.zinelabidine@apeiron-tech.com");
        adminUser.setActivated(true);
        adminUser.setLangKey("fr");
        adminUser.setCreatedBy(adminUser.getLogin());
        adminUser.setCreatedDate(Instant.now());
        adminUser.getAuthorities().add(adminAuthority);
        adminUser.getAuthorities().add(userAuthority);
        mongoTemplate.save(adminUser);

        adminUser.setId("user-4");
        adminUser.setLogin("fahmi.boumaiza@apeiron-tech.com");
        adminUser.setPassword("$2a$10$1xi7b7xBie/DCiFF9u2Bme0c1e.f6jJM9i76kA0e1C4Ues0dGea2u");
        adminUser.setFirstName("Fahmi");
        adminUser.setLastName("Boumaiza");
        adminUser.setEmail("fahmi.boumaiza@apeiron-tech.com");
        adminUser.setActivated(true);
        adminUser.setLangKey("fr");
        adminUser.setCreatedBy(adminUser.getLogin());
        adminUser.setCreatedDate(Instant.now());
        adminUser.getAuthorities().add(adminAuthority);
        adminUser.getAuthorities().add(userAuthority);
        mongoTemplate.save(adminUser);

        adminUser.setId("user-5");
        adminUser.setLogin("abdallah.ghandour@apeiron-tech.com");
        adminUser.setPassword("$2a$10$1xi7b7xBie/DCiFF9u2Bme0c1e.f6jJM9i76kA0e1C4Ues0dGea2u");
        adminUser.setFirstName("Abdallah");
        adminUser.setLastName("Ghandour");
        adminUser.setEmail("abdallah.ghandour@apeiron-tech.com");
        adminUser.setActivated(true);
        adminUser.setLangKey("fr");
        adminUser.setCreatedBy(adminUser.getLogin());
        adminUser.setCreatedDate(Instant.now());
        adminUser.getAuthorities().add(adminAuthority);
        adminUser.getAuthorities().add(userAuthority);
        mongoTemplate.save(adminUser);

        adminUser.setId("user-6");
        adminUser.setLogin("hamza.abdessalem@apeiron-tech.com");
        adminUser.setPassword("$2a$10$1xi7b7xBie/DCiFF9u2Bme0c1e.f6jJM9i76kA0e1C4Ues0dGea2u");
        adminUser.setFirstName("Hamza");
        adminUser.setLastName("Abdessalem");
        adminUser.setEmail("hamza.abdessalem@apeiron-tech.com");
        adminUser.setActivated(true);
        adminUser.setLangKey("fr");
        adminUser.setCreatedBy(adminUser.getLogin());
        adminUser.setCreatedDate(Instant.now());
        adminUser.getAuthorities().add(adminAuthority);
        adminUser.getAuthorities().add(userAuthority);
        mongoTemplate.save(adminUser);
        
        
        adminUser = new User();
        adminUser.setId("user-7");
        adminUser.setLogin("bahriwalid19@gmail.com");
        adminUser.setPassword("$2a$10$1xi7b7xBie/DCiFF9u2Bme0c1e.f6jJM9i76kA0e1C4Ues0dGea2u");
        adminUser.setFirstName("Walid");
        adminUser.setLastName("Bahri");
        adminUser.setEmail("bahriwalid19@gmail.com");
        adminUser.setActivated(true);
        adminUser.setLangKey("fr");
        adminUser.setCreatedBy(adminUser.getLogin());
        adminUser.setCreatedDate(Instant.now());
        adminUser.getAuthorities().add(adminAuthority);
        adminUser.getAuthorities().add(userAuthority);
        mongoTemplate.save(adminUser);
    }
    

}
