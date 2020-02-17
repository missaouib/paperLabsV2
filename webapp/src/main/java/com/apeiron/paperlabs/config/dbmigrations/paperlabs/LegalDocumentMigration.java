package com.apeiron.paperlabs.config.dbmigrations.paperlabs;

import com.apeiron.paperlabs.config.dbmigrations.paperlabs.constants.AuthorsConstants;
import com.apeiron.paperlabs.domain.Company;
import com.apeiron.paperlabs.domain.DescriptionSection;
import com.apeiron.paperlabs.domain.Employer;
import com.apeiron.paperlabs.domain.LegalDocument;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.Instant;
import java.util.*;

/**
 * Creates the initial LegalDocument entities.
 */
@ChangeLog(order = "002")
public class LegalDocumentMigration {

    @ChangeSet(order = "01", author = AuthorsConstants.BELHASSEN_ZINELABIDINE , id = "01-addCdiLegalDocument")
    public void addCdiLegalDocument(MongoTemplate mongoTemplate) {
        LegalDocument legalDocument;

        List<DescriptionSection> descriptionSections = new ArrayList<>();
        DescriptionSection descriptionSection;
        List<String> autoFillConcernedEntities = new ArrayList<>();
        Map<String, String> companyAutoFillInputIdsByFieldName = new LinkedHashMap<>();
        Map<String, String> employerAutoFillInputIdsByFieldName = new LinkedHashMap<>();
        List<Map<String, String>> companiesAutoFillInputIdsByFieldName = new LinkedList<>();
        List<Map<String, String>> employersAutoFillInputIdsByFieldName = new LinkedList<>();

        companyAutoFillInputIdsByFieldName.put("companyName", "input_dénomination_de_la_societé");
        companyAutoFillInputIdsByFieldName.put("companyCapital", "input_capital_social_de_la_societé");
        companyAutoFillInputIdsByFieldName.put("companyUniqueIdentification", "input_identifiant_unique_de_la_societé");
        companyAutoFillInputIdsByFieldName.put("companyRepresentativeFullName", "input_nomPrénom_du_representant_de_la_societé");
        companyAutoFillInputIdsByFieldName.put("companyFullAddress", "input_adresse_de_la_societé");
        companyAutoFillInputIdsByFieldName.put("companyType", "input_type_de_la_societé");
        companiesAutoFillInputIdsByFieldName.add(companyAutoFillInputIdsByFieldName);

        employerAutoFillInputIdsByFieldName.put("employerCinNumber", "input_cin_employeur");
        employerAutoFillInputIdsByFieldName.put("employerFullName", "input_nomPrénom_employeur");
        employerAutoFillInputIdsByFieldName.put("employerFullAddress", "input_adresse_employeur");
        employersAutoFillInputIdsByFieldName.add(employerAutoFillInputIdsByFieldName);

        autoFillConcernedEntities.add(Company.class.getSimpleName());
        autoFillConcernedEntities.add(Employer.class.getSimpleName());

        legalDocument = new LegalDocument();
        legalDocument.setId("5dad0d7dd1a76c479c02aff6");
        legalDocument.setShortName("CDI");
        legalDocument.setAutoFillConcernedEntities(autoFillConcernedEntities);
        legalDocument.setFullName("Contrat de travail à durée indéterminée");
        legalDocument.setKeywords("contrat cdi gérer entreprise");
        legalDocument.setDescription("MODÈLE DE CONTRAT DE TRAVAIL CDI");
        legalDocument.setTemplatePreviewPath("empty_preview");
        legalDocument.setTemplateFilePath("cdi_docx_model.docx");
        legalDocument.setStepperConfigFilePath("cdi_stepper_configuration.json");
        legalDocument.setWorkflowConfigFilePath("cdi_workflow_configuration.json");
        legalDocument.setCategoryId("empty_category");
        legalDocument.setLawyerId("empty_lawyer");
        legalDocument.setEmployersAutoFillInputIdsByFieldName(employersAutoFillInputIdsByFieldName);
        legalDocument.setCompaniesAutoFillInputIdsByFieldName(companiesAutoFillInputIdsByFieldName);
        legalDocument.setCreatedDate(Instant.now());

        descriptionSection = new DescriptionSection();
        descriptionSection.setTitle("Pour qui ? Pourquoi ?");
        descriptionSection.setContent("Le contrat de travail à durée indéterminée (CDI) constitue le contrat de droit commun applicable entre un salarié et l’entreprise ou le particulier qui l’emploie dans le cadre d’une relation de travail. Toute autre forme de contrat de travail ne peut être conclue que dans des cas d’exception.\n\nL’employeur peut être :\n\n- toute personne morale (société, association) ou entreprise individuelle ;\n\n- mais également un particulier qui a recours à un salarié employé à son domicile ou dans l'une de ses résidences (voir dans ce cas les spécificités du contrat ci-après).\n\nToute personne physique a le droit d’être salarié. Il existe toutefois des restrictions applicables aux mineurs et aux étrangers (qui doivent être autoriser à travailler en France).");
        descriptionSections.add(descriptionSection);

        descriptionSection = new DescriptionSection();
        descriptionSection.setTitle("Durée indéterminée - Période d'essai");
        descriptionSection.setContent("Le CDI est par essence à durée indéterminée : il est donc conclu sans limitation de durée. On le distinguera donc bien du contrat de travail à durée déterminée (CDD) et de l'avenant au contrat de travail.\n\nIl peut toutefois prévoir une période d’essai au terme de laquelle les parties pourront mettre fin au contrat sans justification particulière et sans indemnité.");
        descriptionSections.add(descriptionSection);

        descriptionSection = new DescriptionSection();
        descriptionSection.setTitle("Durée de travail");
        descriptionSection.setContent("Le CDI peut être conclu à temps plein ou à temps partiel.\n\nLe contrat de travail à temps plein sera conclu en principe sur la base d’un temps de travail hebdomadaire de 35 heures. Certaines fonctions telle que celle de cadre dirigeant peuvent toutefois justifier de déroger aux 35 heures hebdomadaires. De la même manière, certaines conventions collectives autorisent les parties à prévoir des forfaits jours ou heures au titre permettant une plus grande liberté dans l’organisation de l’emploi du temps du salarié. le salarié peut faire des heures supplémentaires au delà de son contingent annuel d'heures au titre de son contrant de travail, tout en ayant droit a un repos compensateur. Il est à souligner que le temps de travail applicable au stagiaire est soumis aux mêmes règles de celles applicables aux salariés de l'entreprise.\n\nNB : pour la loi qui encadre les stages, il existe quelques nouvelles règles qui ont changé complètement la donne.");
        descriptionSections.add(descriptionSection);

        descriptionSection = new DescriptionSection();
        descriptionSection.setTitle("Forme du CDI");
        descriptionSection.setContent("Pour les entreprises, le CDI, lorsqu’il est à temps plein, peut ne pas être conclu par écrit. C’est la raison pour laquelle beaucoup de relations de travail sont requalifiées en CDI. Le CDI à temps plein naît ainsi de la relation effective entre les deux parties qui sera qualifiée de relation entre un salarié et son employeur si les conditions du CDI son réunies.\n\nIl est toutefois fortement recommandé de conclure un écrit.\n\nCertaines conventions collectives imposent par ailleurs que le CDI fasse l’objet d’un écrit.\n\nEnfin, le CDI à temps partiel devra nécessairement être écrit.\n\nLe CDI devra être conclu en langue française. Lorsque les fonctions sont indiquées dans une langue étrangère (très souvent en Anglais) n’ayant pas d’équivalent en Français, alors le CDI devra comporter une définition des termes concernés.\n\nPour les employeurs particuliers, le chèque emploi-service peut être utilisé pour des prestations de travail occasionnelles dont la durée hebdomadaire n'excède pas 8 heures ou pour une durée dans l'année d'un mois non renouvelable. Pour ces emplois, le chèque emploi-service tient lieu de contrat de travail. En revanche, quel que soit le type de contrat, l’obligation d’écrire un écrit est impérative à partir du moment où la durée du contrat excède 8 heures par semaine ou 4 semaines consécutives sur l’année.");
        descriptionSections.add(descriptionSection);

        descriptionSection = new DescriptionSection();
        descriptionSection.setTitle("Contenu du CDI");
        descriptionSection.setContent("Les termes du CDI sont librement négociés entre l’employeur et le salarié. Ils pourront ainsi prévoir des clauses particulières telles que la clause de non-concurrence avec toutes ses conditions de validité, la clause d’exclusivité, la clause de mobilité ou encore la clause de dédit-formation ou une clause de hardship.\n\nLe contrat devra toutefois respecter les dispositions légales (prévues par le Code du travail) et celles de la convention collective applicable, qu’elles s’appliquent au CDI à temps plein ou à d’autres formes de CDI tel que le CDI à temps partiel.\n\nEnfin, certaines clauses contraires à l’ordre public seront expressément interdites (notamment les clauses discriminatoires, les clauses stipulant une rémunération inférieure au minimum légal, etc.).");
        descriptionSections.add(descriptionSection);

        descriptionSection = new DescriptionSection();
        descriptionSection.setTitle("Fin du CDI");
        descriptionSection.setContent("Le CDI étant à durée indéterminée, il ne peut y être mis un terme que par la volonté (ou le décès) de l’une des parties ou des deux parties.\n\nLe CDI peut donc prendre fin à l’initiative du salarié, par sa démission ou son départ volontaire à la retraite, ou à l’initiative de l’employeur par un licenciement ou une mise à la retraite d'office).\n\nIl peut également être terminé d’un commun accord entre le salarié et l’employeur, au titre d’une rupture conventionnelle.\n\nLorsque il y'a une rupture du CDI à l’initiative de l’employeur, le salarié a droit, dans la majeure partie des cas, à des indemnité dont la nature et les montants varient selon la forme et la cause du licenciement, ainsi que l’ancienneté du salarié (et, le cas échéant, toute autre clause particulière du CDI).");
        descriptionSections.add(descriptionSection);

        descriptionSection = new DescriptionSection();
        descriptionSection.setTitle("Spécificité des particuliers employeur : a-t-on l’obligation de conclure un contrat de travail écrit ?");
        descriptionSection.setContent("Le chèque emploi-service peut être utilisé pour des prestations de travail occasionnelles dont la durée hebdomadaire n'excède pas 8 heures ou pour une durée dans l'année d'un mois non renouvelable. Pour ces emplois, le chèque emploi-service tient lieu de contrat de travail.\n\nAinsi, quel que soit le type de contrat, l’obligation d’écrire un écrit est impérative à partir du moment où la durée du contrat excède 8 heures par semaine ou 4 semaines consécutives sur l’année.");
        descriptionSections.add(descriptionSection);

        descriptionSection = new DescriptionSection();
        descriptionSection.setTitle("Particulier employeur : quelles sont les dispositions spécifiques du CDI ?");
        descriptionSection.setContent("La loi prévoit des exclusions spécifiques s’agissant du particulier employeur.\n\nAinsi, les dispositions légales sur la durée du travail ne sont pas applicables aux employés de maison qui exercent leur profession au domicile de leur employeur et qui sont soumis aux dispositions de la convention collective nationale (CCN) des salariés du particulier employeur ainsi qu’à l’article L7221-2 du Code du travail.\n\nSelon les dispositions conventionnelles, le contrat de travail est à temps plein ou, s'il est conclu pour une durée inférieure à 40 heures par semaine, à temps partiel.\n\nPour les contrats supérieurs à 8 heures par semaine, un écrit doit être formalisé et doit préciser la durée hebdomadaire ou mensuelle du contrat de travail.\n\nLes heures de présence responsable ne concernent que les emplois à caractère familial. Ce sont les heures où le salarié peut utiliser son temps pour lui-même tout en restant vigilant pour intervenir s’il y a lieu (lors de la sieste d’un enfant par exemple).\n\nLes heures de présence responsables sont rémunérées aux 2/3 du salaire conventionnel de base.\n\nEn cas de garde partagée (une nounou embauchée par plusieurs familles pour la garde en commun de leurs enfants), un contrat de travail écrit est établi avec le salarié par chaque famille d’employeur.\n\nLa durée du travail s’entend du total des heures effectuées au domicile de l’une et de l’autre.\n\nChaque famille rémunère les heures effectuées à son domicile.\n\nLa date des congés est fixée par les deux employeurs d’un commun accord.\n\nLe contrat inclut une clause identique précisant le lien avec l’autre famille employeur.\n\nLa rupture de l’un des contrats constitue une modification substantielle de l’autre : dans ce cas, la famille qui n’a pas rompu le contrat, doit proposer au salarié soit de continuer la relation contractuelle avec elle, soit d’augmenter la durée du temps de travail. Le salarié doit alors bénéficier d’un délai de réflexion suffisant, et son refus ne peut en aucun cas être fautif (et donnera lieu à une rupture de contrat non disciplinaire assortie des indemnités légales de rupture).\n\nLa médecine du travail à la charge des deux employeurs.");
        descriptionSections.add(descriptionSection);

        legalDocument.setDescriptionSections(descriptionSections);

        legalDocument.setPrice(49.99f);
        List<String> documentsRecommendationId = new ArrayList<>();
        documentsRecommendationId.add("5dad0d7dd1a76c479c02aff8");
        legalDocument.setDocumentsRecommendationId(documentsRecommendationId);

        mongoTemplate.save(legalDocument);
    }

    //TODO : fix hardcoded save employee and company data
    /*@ChangeSet(order = "02", author = AuthorsConstants.FAHMI_BOUMAIZA , id = "02-addTestLegalDocument")
    public void addTestLegalDocument(MongoTemplate mongoTemplate) {
        LegalDocument legalDocument;
        legalDocument = new LegalDocument();
        legalDocument.setShortName("Test");
        legalDocument.setFullName("Document test");
        legalDocument.setKeywords("contrat");
        legalDocument.setDescription("THIS IS A TEST");
        legalDocument.setTemplatePreviewPath("empty_preview");
        legalDocument.setTemplateFilePath("test_docx_model.docx");
        legalDocument.setStepperConfigFilePath("test_stepper_configuration.json");
        legalDocument.setWorkflowConfigFilePath("test_workflow_configuration.json");
        legalDocument.setCategoryId("empty_category");
        legalDocument.setLawyerId("empty_lawyer");

        mongoTemplate.save(legalDocument);
    }*/

    @ChangeSet(order = "03", author = AuthorsConstants.FAHMI_BOUMAIZA , id = "03-addAccordDeConfidentialiteLegalDocument")
    public void addAccordDeConfidentialiteLegalDocument(MongoTemplate mongoTemplate) {
        LegalDocument legalDocument;

        List<DescriptionSection> descriptionSections = new ArrayList<>();
        DescriptionSection descriptionSection;
        List<String> autoFillConcernedEntities = new ArrayList<>();
        Map<String, String> companyPrestataireAutoFillInputIdsByFieldName = new LinkedHashMap<>();
        Map<String, String> companyBeneficiaireAutoFillInputIdsByFieldName = new LinkedHashMap<>();
        List<Map<String, String>> companiesAutoFillInputIdsByFieldName = new LinkedList<>();

        companyPrestataireAutoFillInputIdsByFieldName.put("companyName", "input_dénomination_de_la_societé_prestataire");
        companyPrestataireAutoFillInputIdsByFieldName.put("companyCapital", "input_capital_social_de_la_societé_prestataire");
        companyPrestataireAutoFillInputIdsByFieldName.put("companyUniqueIdentification", "input_identifiant_unique_de_la_societé_prestataire");
        companyPrestataireAutoFillInputIdsByFieldName.put("companyRepresentativeFullName", "input_nomPrénom_du_representant_de_la_societé_prestataire");
        companyPrestataireAutoFillInputIdsByFieldName.put("companyFullAddress", "input_adresse_de_la_societé_prestataire");
        companyPrestataireAutoFillInputIdsByFieldName.put("companyType", "input_type_de_la_societé_prestataire");
        companiesAutoFillInputIdsByFieldName.add(companyPrestataireAutoFillInputIdsByFieldName);

        companyBeneficiaireAutoFillInputIdsByFieldName.put("companyName", "input_dénomination_de_la_societé_bénéficiaire");
        companyBeneficiaireAutoFillInputIdsByFieldName.put("companyCapital", "input_capital_social_de_la_societé_bénéficiaire");
        companyBeneficiaireAutoFillInputIdsByFieldName.put("companyUniqueIdentification", "input_identifiant_unique_de_la_societé_bénéficiaire");
        companyBeneficiaireAutoFillInputIdsByFieldName.put("companyRepresentativeFullName", "input_nomPrénom_du_representant_de_la_societé_bénéficiaire");
        companyBeneficiaireAutoFillInputIdsByFieldName.put("companyFullAddress", "input_adresse_de_la_societé_bénéficiaire");
        companyBeneficiaireAutoFillInputIdsByFieldName.put("companyType", "input_type_de_la_societé_bénéficiaire");

        companiesAutoFillInputIdsByFieldName.add(companyBeneficiaireAutoFillInputIdsByFieldName);

        autoFillConcernedEntities.add(Company.class.getSimpleName());

        legalDocument = new LegalDocument();
        legalDocument.setId("5dad0d7dd1a76c479c02aff8");
        legalDocument.setAutoFillConcernedEntities(autoFillConcernedEntities);
        legalDocument.setShortName("NDA");
        legalDocument.setFullName("Accord de confidentialité");
        legalDocument.setKeywords("accord confidentialité gérer entreprise");
        legalDocument.setDescription("MODÈLE D'ACCORD DE CONFIDENTIALITE");
        legalDocument.setTemplatePreviewPath("empty_preview");
        legalDocument.setTemplateFilePath("accord_de_confidentialite_docx_model.docx");
        legalDocument.setStepperConfigFilePath("accord_de_confidentialite_stepper_configuration.json");
        legalDocument.setWorkflowConfigFilePath("accord_de_confidentialite_workflow_configuration.json");
        legalDocument.setCategoryId("empty_category");
        legalDocument.setLawyerId("empty_lawyer");
        legalDocument.setCompaniesAutoFillInputIdsByFieldName(companiesAutoFillInputIdsByFieldName);
        legalDocument.setCreatedDate(Instant.now());

        descriptionSection = new DescriptionSection();
        descriptionSection.setTitle("Qu’est ce que l’accord de confidentialité ?");
        descriptionSection.setContent("L’accord de confidentialité, également connu sous son nom anglais “Non disclosure agreement” ou “NDA”, est un contrat dont l’objet est d’encadrer des discussions d’affaires par un engagement de confidentialité.\n" +
            "\n" +
            "Il est en effet très fréquent que des entreprises, lorsqu’elles se rapprochent pour discuter de l’opportunité d’un projet commun, s’échangent des informations sensibles sur leurs activités respectives, qui pourrait être révélées à la concurrence ou utilisées par l’autre partie pour ses propres besoins alors même que les discussions seraient rompues.\n" +
            "\n" +
            "Celles-ci veulent alors s’assurer que les informations qui sont révélées dans ce cadre demeurent strictement confidentialité. Elles doivent donc exiger la signature d’un accord de confidentialité.\n" +
            "\n" +
            "Le contenu d'un accord de confidentialité est primordial opur garantir l'intégrité des deux parties. Il convient donc de faire extrèmement attention lors de la rédaction d'un accord de confidentialtié en sécurisant le plus de positions possibles pour son entreprise.");
        descriptionSections.add(descriptionSection);

        descriptionSection = new DescriptionSection();
        descriptionSection.setTitle("Dans quels cas est-il recommandé de signer un accord de confidentialité ?");
        descriptionSection.setContent("La signature d’un accord de confidentialité ou NDA est fortement recommandée dans toutes les situations où au moins deux professionnels entament des discussions d’affaires.");
        descriptionSections.add(descriptionSection);

        descriptionSection = new DescriptionSection();
        descriptionSection.setTitle("Accord de confidentialité réciproque ou unilatéral ?");
        descriptionSection.setContent("L’accord de confidentialité ou NDA peut être réciproque (synallagmatique) ou simplement unilatéral.");
        descriptionSections.add(descriptionSection);

        descriptionSection = new DescriptionSection();
        descriptionSection.setTitle("Toutes les informations échangées peuvent-elles être confidentielles ?");
        descriptionSection.setContent("Toutes les informations échangées lors des discussions ne peuvent pas avoir un caractère confidentiel. Dans certains cas, il ne peut en effet pas être reproché aux parties de les avoir ultérieurement divulguées.");
        descriptionSections.add(descriptionSection);

        descriptionSection = new DescriptionSection();
        descriptionSection.setTitle("Pourquoi prévoir une durée à l’accord de confidentialité ?");
        descriptionSection.setContent("L’accord de confidentialité est un contrat comme un autre. Dès lors, s’il est prévu à durée indéterminée, il pourra être unilatéralement résilié par n’importe quelle partie à tout moment. Celle-ci ne sera dès lors plus liée par l’obligation de confidentialité.\n" +
            "\n" +
            "C’est pour cette raison qu’il est préférable de prévoir un accord de confidentialité avec une durée déterminée, afin qu’aucune résiliation unilatérale ne puisse intervenir.\n" +
            "\n" +
            "La durée prévue sera plus ou moins longue selon la durée de protection souhaitée, ce qui s’apprécie en fonction de la durée de “fraîcheur” des informations sensibles communiquées.\n" +
            "\n" +
            "Par exemple, certaines informations sur la clientèle peuvent être très précieuses pendant 6 mois, durée au delà de laquelle leur révélation ne présentera plus de danger pour l’entreprise concernée.");
        descriptionSections.add(descriptionSection);

        descriptionSection = new DescriptionSection();
        descriptionSection.setTitle("Que se passe-t-il à la fin de la durée de validité de l’accord de confidentialité ?");
        descriptionSection.setContent("A la fin de la durée de validité de l’accord de confidentialité, si les discussions n’ont pas abouti, alors les parties sont supposées détruire les supports contenant les informations échangées.\n" +
            "\n" +
            "Elle ne devraient ainsi pas pouvoir disposer des informations recueillies comme elles l’entendent.\n" +
            "\n" +
            "En pratique, si elles se servent de telles informations, par exemple en les divulgant ou en concurrençant la partie qui les a révélées sur la base des informations recueillies lors des échanges couverts par l’accord de confidentialité, elle peuvent s’exposer alors à une action en concurrence déloyale et pourrait être amenée à verser des dommages-intérêts.\n" +
            "\n" +
            "Si les négociations ont abouti, alors il est recommandé de perpétuer l’engagement de confidentialité en prévoyant une clause de confidentialité dans l’accord final entre les parties (contrat commercial, contrat de freelance, pacte d’associés, etc.).\n" +
            "\n" +
            "L'accord de confidentialité prendra alors fin en tant que contrat mais l'engagement de confidentialité sera repris dans un nouveau document contractuel plus global matérialisant l'accord des parties.");
        descriptionSections.add(descriptionSection);

        descriptionSection = new DescriptionSection();
        descriptionSection.setTitle("Quelles sont les sanctions de la violation de l’accord de confidentialité ?");
        descriptionSection.setContent("La violation de l’accord de confidentialité est une faute contractuelle qui entraîne pas possible mise en jeu de la responsabilité civile de la partie concernée : elle peut donc être amenée à payer des dommages-intérêts à la partie victime ou être forcée, si cela est possible, de rectifier son acte.\n" +
            "\n" +
            "La partie victime devra bien prouver que les informations divulguée étaient couvertes par l’accord de confidentialité. Il est donc important de matérialiser, lors des discussion, les échanges d’informations de sorte à laisser des traces (envois d’e-mails, remise de documents avec accusé de réception, etc.).");
        descriptionSections.add(descriptionSection);

        legalDocument.setDescriptionSections(descriptionSections);

        legalDocument.setPrice(49.99f);

        legalDocument.setDocumentsRecommendationId(new ArrayList<>());

        mongoTemplate.save(legalDocument);
    }

    @ChangeSet(order = "04", author = AuthorsConstants.FAHMI_BOUMAIZA , id = "04-addStatusSarlLegalDocument")
    public void addStatusSarlLegalDocument(MongoTemplate mongoTemplate) {
        LegalDocument legalDocument;

        List<DescriptionSection> descriptionSections = new ArrayList<>();
        DescriptionSection descriptionSection;
        List<String> autoFillConcernedEntities = new ArrayList<>();

        autoFillConcernedEntities.add(Company.class.getSimpleName());

        legalDocument = new LegalDocument();
        legalDocument.setId("5dad0d7dd1a76c479c02affa");
        legalDocument.setShortName("SARL");
        legalDocument.setAutoFillConcernedEntities(autoFillConcernedEntities);
        legalDocument.setFullName("Status de societé à responsabilité limitée");
        legalDocument.setKeywords("status sarl créer entreprise");
        legalDocument.setDescription("MODÈLE STATUS SARL");
        legalDocument.setTemplatePreviewPath("empty_preview");
        legalDocument.setTemplateFilePath("status_sarl_docx_model.docx");
        legalDocument.setStepperConfigFilePath("status_sarl_stepper_configuration.json");
        legalDocument.setWorkflowConfigFilePath("status_sarl_workflow_configuration.json");
        legalDocument.setCategoryId("empty_category");
        legalDocument.setLawyerId("empty_lawyer");
        legalDocument.setCreatedDate(Instant.now());

        descriptionSection = new DescriptionSection();
        descriptionSection.setTitle("Création de SARL :");
        descriptionSection.setContent("Procédure 100% en ligne pour créer votre SARL en toute simplicité.\n" +
            "Statuts générés en 10mn.");
        descriptionSections.add(descriptionSection);

        legalDocument.setDescriptionSections(descriptionSections);

        legalDocument.setPrice(49.99f);

        legalDocument.setDocumentsRecommendationId(new ArrayList<>());

        mongoTemplate.save(legalDocument);
    }

    @ChangeSet(order = "05", author = AuthorsConstants.BELHASSEN_ZINELABIDINE , id = "05-addPvNominationNouveauGerantLegalDocument")
    public void addPvNominationNouveauGerantLegalDocument(MongoTemplate mongoTemplate) {
        LegalDocument legalDocument;

        List<DescriptionSection> descriptionSections = new ArrayList<>();
        DescriptionSection descriptionSection;
        List<String> autoFillConcernedEntities = new ArrayList<>();
        Map<String, String> companyAutoFillInputIdsByFieldName = new LinkedHashMap<>();
        Map<String, String> employerAutoFillInputIdsByFieldName = new LinkedHashMap<>();
        List<Map<String, String>> companiesAutoFillInputIdsByFieldName = new LinkedList<>();
        List<Map<String, String>> employersAutoFillInputIdsByFieldName = new LinkedList<>();

        companiesAutoFillInputIdsByFieldName.add(companyAutoFillInputIdsByFieldName);
        employersAutoFillInputIdsByFieldName.add(employerAutoFillInputIdsByFieldName);

        autoFillConcernedEntities.add(Company.class.getSimpleName());
        autoFillConcernedEntities.add(Employer.class.getSimpleName());

        legalDocument = new LegalDocument();
        legalDocument.setId("5dde43ffd02a3b36803e18b8");
        legalDocument.setShortName("PV-NNG");
        legalDocument.setAutoFillConcernedEntities(autoFillConcernedEntities);
        legalDocument.setFullName("Procès-Verbal : Nomination d'un Nouveau Gérant");
        legalDocument.setKeywords("pv nng gérer entreprise");
        legalDocument.setDescription("MODÈLE D'UN PROCÈS-VERBAL : NOMINATION D'UN NOUVEAU GÉRANT");
        legalDocument.setTemplatePreviewPath("empty_preview");
        legalDocument.setTemplateFilePath("pv_nomination_nouveau_gerant_docx_model.docx");
        legalDocument.setStepperConfigFilePath("pv_nomination_nouveau_gerant_stepper_configuration.json");
        legalDocument.setWorkflowConfigFilePath("pv_nomination_nouveau_gerant_workflow_configuration.json");
        legalDocument.setCategoryId("empty_category");
        legalDocument.setLawyerId("empty_lawyer");
        legalDocument.setEmployersAutoFillInputIdsByFieldName(employersAutoFillInputIdsByFieldName);
        legalDocument.setCompaniesAutoFillInputIdsByFieldName(companiesAutoFillInputIdsByFieldName);
        legalDocument.setCreatedDate(Instant.now());

        descriptionSection = new DescriptionSection();
        descriptionSection.setTitle("Création de Procès-Verbal : Nomination d'un Nouveau Gérant :");
        descriptionSection.setContent("Procédure 100% en ligne pour créer votre document en toute simplicité.");
        descriptionSections.add(descriptionSection);

        legalDocument.setDescriptionSections(descriptionSections);

        legalDocument.setPrice(10.59f);

        List<String> documentsRecommendationId = new ArrayList<>();
        legalDocument.setDocumentsRecommendationId(documentsRecommendationId);

        mongoTemplate.save(legalDocument);

    }

    @ChangeSet(order = "06", author = AuthorsConstants.FAHMI_BOUMAIZA , id = "06-addStatusCDILegalDocument")
    public void addStatusCDILegalDocument(MongoTemplate mongoTemplate) {     
        LegalDocument legalDocument;

        List<DescriptionSection> descriptionSections = new ArrayList<>();
        DescriptionSection descriptionSection;
        List<String> autoFillConcernedEntities = new ArrayList<>();

        autoFillConcernedEntities.add(Company.class.getSimpleName());

        legalDocument = new LegalDocument();
        legalDocument.setId("5dad0d7dd1a76c479c02affa99");
        legalDocument.setShortName("CDI");
        legalDocument.setAutoFillConcernedEntities(autoFillConcernedEntities);
        legalDocument.setFullName("Status de societé à responsabilité limitée");
        legalDocument.setKeywords(" contrat cdi créer droit de travail");
        legalDocument.setDescription("MODÈLE STATUS CDI");
        legalDocument.setTemplatePreviewPath("empty_preview");
        legalDocument.setTemplateFilePath("status_cdi_docx_model.docx");
        legalDocument.setStepperConfigFilePath("status_cdi_stepper_configuration.json");
        legalDocument.setWorkflowConfigFilePath("status_cdi_workflow_configuration.json");
        legalDocument.setCategoryId("empty_category");
        legalDocument.setLawyerId("empty_lawyer");
        legalDocument.setCreatedDate(Instant.now());

        descriptionSection = new DescriptionSection();
        descriptionSection.setTitle("Création de CDI:");
        descriptionSection.setContent("Procédure 100% en ligne pour créer votre CDI en toute simplicité.\n" +
            "Statuts générés en 10mn.");
        descriptionSections.add(descriptionSection);

        legalDocument.setDescriptionSections(descriptionSections);

        legalDocument.setPrice(49.99f);

        legalDocument.setDocumentsRecommendationId(new ArrayList<>());

        mongoTemplate.save(legalDocument);
    }
    @ChangeSet(order = "07", author = AuthorsConstants.FAHMI_BOUMAIZA , id = "07-addCDDLegalDocument")
    public void addCDDLegalDocument(MongoTemplate mongoTemplate) {     
        LegalDocument legalDocument;

        List<DescriptionSection> descriptionSections = new ArrayList<>();
        DescriptionSection descriptionSection;
        List<String> autoFillConcernedEntities = new ArrayList<>();

        autoFillConcernedEntities.add(Company.class.getSimpleName());

        legalDocument = new LegalDocument();
        legalDocument.setId("5dad0d7dd1a76c479c02affa97");
        legalDocument.setShortName("CDD");
        legalDocument.setAutoFillConcernedEntities(autoFillConcernedEntities);
        legalDocument.setFullName("Contrat de travail à durée déterminée");
        legalDocument.setKeywords(" contrat cdd créer droit de travail");
        legalDocument.setDescription("MODÈLE DE CONTRAT DE TRAVAIL CDD");
        legalDocument.setTemplatePreviewPath("empty_preview");
        legalDocument.setTemplateFilePath("cdd_docx_model.docx");
        legalDocument.setStepperConfigFilePath("cdd_stepper_configuration.json");
        legalDocument.setWorkflowConfigFilePath("cdd_workflow_configuration.json");
        legalDocument.setCategoryId("empty_category");
        legalDocument.setLawyerId("empty_lawyer");
        legalDocument.setCreatedDate(Instant.now());

        descriptionSection = new DescriptionSection();
        descriptionSection.setTitle("Création de CDD:");
        descriptionSection.setContent("Procédure 100% en ligne pour créer votre CDD en toute simplicité.\n" +
            "Statuts générés en 10mn.");
        descriptionSections.add(descriptionSection);

        legalDocument.setDescriptionSections(descriptionSections);

        legalDocument.setPrice(49.99f);

        legalDocument.setDocumentsRecommendationId(new ArrayList<>());

        mongoTemplate.save(legalDocument);
    }
    @ChangeSet(order = "08", author = AuthorsConstants.FAHMI_BOUMAIZA , id = "08-addCIVPLegalDocument")
    public void addCIVPLegalDocument(MongoTemplate mongoTemplate) {     
        LegalDocument legalDocument;

        List<DescriptionSection> descriptionSections = new ArrayList<>();
        DescriptionSection descriptionSection;
        List<String> autoFillConcernedEntities = new ArrayList<>();

        autoFillConcernedEntities.add(Company.class.getSimpleName());

        legalDocument = new LegalDocument();
        legalDocument.setId("5dad0d7dd1a76c479c02affa95");
        legalDocument.setShortName("CIVP");
        legalDocument.setAutoFillConcernedEntities(autoFillConcernedEntities);
        legalDocument.setFullName("Contrat d'initiation à la vie professionnelle");
        legalDocument.setKeywords(" contrat civp créer droit de travail");
        legalDocument.setDescription("MODÈLE DE CONTRAT DE TRAVAIL D'INITIATION A LA VIE PROFESSIONNELLE");
        legalDocument.setTemplatePreviewPath("empty_preview");
        legalDocument.setTemplateFilePath("civp_docx_model.docx");
        legalDocument.setStepperConfigFilePath("civp_stepper_configuration.json");
        legalDocument.setWorkflowConfigFilePath("civp_workflow_configuration.json");
        legalDocument.setCategoryId("empty_category");
        legalDocument.setLawyerId("empty_lawyer");
        legalDocument.setCreatedDate(Instant.now());

        descriptionSection = new DescriptionSection();
        descriptionSection.setTitle("Création de CIVP:");
        descriptionSection.setContent("Procédure 100% en ligne pour créer votre CIVP en toute simplicité.\n" +
            "Statuts générés en 10mn.");
        descriptionSections.add(descriptionSection);

        legalDocument.setDescriptionSections(descriptionSections);

        legalDocument.setPrice(49.99f);

        legalDocument.setDocumentsRecommendationId(new ArrayList<>());

        mongoTemplate.save(legalDocument);
    }
    
    @ChangeSet(order = "09", author = AuthorsConstants.FAHMI_BOUMAIZA , id = "06-addLettreDeDemissionLegalDocument")
    public void addLettreDeDemissionLegalDocument(MongoTemplate mongoTemplate) {     
        LegalDocument legalDocument;

        List<DescriptionSection> descriptionSections = new ArrayList<>();
        DescriptionSection descriptionSection;
        List<String> autoFillConcernedEntities = new ArrayList<>();

        autoFillConcernedEntities.add(Company.class.getSimpleName());

        legalDocument = new LegalDocument();
        legalDocument.setId("5dad0d7dd1a76c479c02affa98");
        legalDocument.setShortName("LETTRE DE DEMISSION");
        legalDocument.setAutoFillConcernedEntities(autoFillConcernedEntities);
        legalDocument.setFullName("Lettre de demission");
        legalDocument.setKeywords(" lettre de demission gérance");
        legalDocument.setDescription("MODÈLE LETTRE DE DEMISSION");
        legalDocument.setTemplatePreviewPath("empty_preview");
        legalDocument.setTemplateFilePath("lettre_de_demission_docx_model.doc");
        legalDocument.setStepperConfigFilePath("lettre_de_demission_stepper_configuration.json");
        legalDocument.setWorkflowConfigFilePath("lettre_de_demissionworkflow_configuration.json");
        legalDocument.setCategoryId("empty_category");
        legalDocument.setLawyerId("empty_lawyer");
        legalDocument.setCreatedDate(Instant.now());
   
        descriptionSection = new DescriptionSection();
        descriptionSection.setTitle("Création de LETTRE DE DEMISSION:");
        descriptionSection.setContent("Procédure 100% en ligne pour créer votre LETTRE DE DEMISSION en toute simplicité.\n" +
            "Statuts générés en 10mn.");
        descriptionSections.add(descriptionSection);

        legalDocument.setDescriptionSections(descriptionSections);

        legalDocument.setPrice(49.99f);

        legalDocument.setDocumentsRecommendationId(new ArrayList<>());

        mongoTemplate.save(legalDocument);
    } 
    
    @ChangeSet(order = "10", author = AuthorsConstants.FAHMI_BOUMAIZA , id = "10-addAractLegalDocument")
    public void addAractLegalDocument(MongoTemplate mongoTemplate) {     
        LegalDocument legalDocument;

        List<DescriptionSection> descriptionSections = new ArrayList<>();
        DescriptionSection descriptionSection;
        List<String> autoFillConcernedEntities = new ArrayList<>();
        Map<String, String> companyAutoFillInputIdsByFieldName = new LinkedHashMap<>();
        Map<String, String> employerAutoFillInputIdsByFieldName = new LinkedHashMap<>();
        List<Map<String, String>> companiesAutoFillInputIdsByFieldName = new LinkedList<>();
        List<Map<String, String>> employersAutoFillInputIdsByFieldName = new LinkedList<>();
        
        companiesAutoFillInputIdsByFieldName.add(companyAutoFillInputIdsByFieldName);
        employersAutoFillInputIdsByFieldName.add(employerAutoFillInputIdsByFieldName);

        autoFillConcernedEntities.add(Company.class.getSimpleName());
        autoFillConcernedEntities.add(Employer.class.getSimpleName());

        legalDocument = new LegalDocument();
        legalDocument.setId("5dad0d7dd1a76c479c02affa914");
        legalDocument.setShortName("ARACT");
        legalDocument.setAutoFillConcernedEntities(autoFillConcernedEntities);
        legalDocument.setFullName("ARACT");
        legalDocument.setKeywords(" aract gérer droit de travail");
        legalDocument.setDescription("ARACT");
        legalDocument.setTemplatePreviewPath("empty_preview");
        legalDocument.setTemplateFilePath("accord_amiable_docx_model.docx");
        legalDocument.setStepperConfigFilePath("accord_amiable_stepper_configuration.json");
        legalDocument.setWorkflowConfigFilePath("accord_amiable_workflow_configuration.json");
        legalDocument.setCategoryId("empty_category");
        legalDocument.setLawyerId("empty_lawyer");
        legalDocument.setEmployersAutoFillInputIdsByFieldName(employersAutoFillInputIdsByFieldName);
        legalDocument.setCompaniesAutoFillInputIdsByFieldName(companiesAutoFillInputIdsByFieldName);
        legalDocument.setCreatedDate(Instant.now());
        descriptionSection = new DescriptionSection();
        
        descriptionSection.setTitle("Création de ARACT:");
        descriptionSection.setContent("Procédure 100% en ligne pour créer votre ARACT en toute simplicité.\n" +
            "Statuts générés en 10mn.");
        descriptionSections.add(descriptionSection);

        legalDocument.setDescriptionSections(descriptionSections);

        legalDocument.setPrice(49.99f);
        List<String> documentsRecommendationId = new ArrayList<>();
        legalDocument.setDocumentsRecommendationId(documentsRecommendationId);
        mongoTemplate.save(legalDocument);
    }
    @ChangeSet(order = "11", author = AuthorsConstants.FAHMI_BOUMAIZA , id = "11-addLettrededomicilisationLegalDocument")
    public void addLettrededomicilisationLegalDocument(MongoTemplate mongoTemplate) {     
        LegalDocument legalDocument;

        List<DescriptionSection> descriptionSections = new ArrayList<>();
        DescriptionSection descriptionSection;
        List<String> autoFillConcernedEntities = new ArrayList<>();
        Map<String, String> companyAutoFillInputIdsByFieldName = new LinkedHashMap<>();
        Map<String, String> employerAutoFillInputIdsByFieldName = new LinkedHashMap<>();
        List<Map<String, String>> companiesAutoFillInputIdsByFieldName = new LinkedList<>();
        List<Map<String, String>> employersAutoFillInputIdsByFieldName = new LinkedList<>();
        
        companiesAutoFillInputIdsByFieldName.add(companyAutoFillInputIdsByFieldName);

        autoFillConcernedEntities.add(Company.class.getSimpleName());

        legalDocument = new LegalDocument();
        legalDocument.setId("5dad0d7dd1a76c479c02affa915");
        legalDocument.setShortName("ATTESTATION DE DOMICILIATION");
        legalDocument.setAutoFillConcernedEntities(autoFillConcernedEntities);
        legalDocument.setFullName("ATTESTATION DE DOMICILIATION");
        legalDocument.setKeywords(" atttestaion de domicilisation gérer baux");
        legalDocument.setDescription("ATTESTATION DE DOMICILIATION");
        legalDocument.setTemplatePreviewPath("empty_preview");
        legalDocument.setTemplateFilePath("attestation_de_domicilisation_docx_model.docx");
        legalDocument.setStepperConfigFilePath("attestation_de_domicilisation_stepper_configuration.json");
        legalDocument.setWorkflowConfigFilePath("attestation_de_domicilisation_workflow_configuration.json");
        legalDocument.setCategoryId("empty_category");
        legalDocument.setLawyerId("empty_lawyer");
        legalDocument.setEmployersAutoFillInputIdsByFieldName(employersAutoFillInputIdsByFieldName);
        legalDocument.setCompaniesAutoFillInputIdsByFieldName(companiesAutoFillInputIdsByFieldName);
        legalDocument.setCreatedDate(Instant.now());
        descriptionSection = new DescriptionSection();
        
       
      
        descriptionSection.setTitle("Création de ATTESTATION DE DOMICILIATION:");
        descriptionSection.setContent("Procédure 100% en ligne pour créer votre ATTESTATION DE DOMICILIATION en toute simplicité.\n" +
            "Statuts générés en 10mn.");
        descriptionSections.add(descriptionSection);

        legalDocument.setDescriptionSections(descriptionSections);

        legalDocument.setPrice(49.99f);

        legalDocument.setDocumentsRecommendationId(new ArrayList<>());

        mongoTemplate.save(legalDocument);
    }

    
    
    
  
     
    @ChangeSet(order = "12", author = AuthorsConstants.FAHMI_BOUMAIZA , id = "12-addFormulairestatusLegalDocument")
    public void addFormulairestatusLegalDocument(MongoTemplate mongoTemplate) {     
        LegalDocument legalDocument;
  
        List<DescriptionSection> descriptionSections = new ArrayList<>();
        DescriptionSection descriptionSection;
        List<String> autoFillConcernedEntities = new ArrayList<>();
        Map<String, String> companyAutoFillInputIdsByFieldName = new LinkedHashMap<>();
        Map<String, String> employerAutoFillInputIdsByFieldName = new LinkedHashMap<>();
        List<Map<String, String>> companiesAutoFillInputIdsByFieldName = new LinkedList<>();
        List<Map<String, String>> employersAutoFillInputIdsByFieldName = new LinkedList<>();
        
        companiesAutoFillInputIdsByFieldName.add(companyAutoFillInputIdsByFieldName);

        autoFillConcernedEntities.add(Company.class.getSimpleName());

        legalDocument = new LegalDocument();
        legalDocument.setId("5dad0d7dd1a76c479c02affa933");
        legalDocument.setShortName("FORMULAIRE STATUS");
        legalDocument.setAutoFillConcernedEntities(autoFillConcernedEntities);
        legalDocument.setFullName("FORMULAIRE STATUS");
        legalDocument.setKeywords("constitution sociétés");
        legalDocument.setDescription("FORMULAIRE STATUS");
        legalDocument.setTemplatePreviewPath("empty_preview");
        legalDocument.setTemplateFilePath("formulaire_statuts_docx_model.docx");
        legalDocument.setStepperConfigFilePath("formulaire_statuts_docx_model_stepper_configuration.json");
        legalDocument.setWorkflowConfigFilePath("formulaire_statuts_docx_model_workflow_configuration.json");
        legalDocument.setCategoryId("empty_category");
        legalDocument.setLawyerId("empty_lawyer");
        legalDocument.setEmployersAutoFillInputIdsByFieldName(employersAutoFillInputIdsByFieldName);
        legalDocument.setCompaniesAutoFillInputIdsByFieldName(companiesAutoFillInputIdsByFieldName);
        legalDocument.setCreatedDate(Instant.now());
        descriptionSection = new DescriptionSection();
            
       
      
        descriptionSection.setTitle("FORMULAIRE STATUS:");
        descriptionSection.setContent("Procédure 100% en ligne pour créer votre FORMULAIRE STATUS en toute simplicité.\n" +
            "Statuts générés en 10mn.");
        descriptionSections.add(descriptionSection);

        legalDocument.setDescriptionSections(descriptionSections);

        legalDocument.setPrice(49.99f);

        legalDocument.setDocumentsRecommendationId(new ArrayList<>());

        mongoTemplate.save(legalDocument);
    }
   
    @ChangeSet(order = "13", author = AuthorsConstants.FAHMI_BOUMAIZA , id = "13-addconvocationagoLegalDocument")

    public void addconvocationagoLegalDocument(MongoTemplate mongoTemplate) {     
        LegalDocument legalDocument;

        List<DescriptionSection> descriptionSections = new ArrayList<>();
        DescriptionSection descriptionSection;
        List<String> autoFillConcernedEntities = new ArrayList<>();
        Map<String, String> companyAutoFillInputIdsByFieldName = new LinkedHashMap<>();
        Map<String, String> employerAutoFillInputIdsByFieldName = new LinkedHashMap<>();
        List<Map<String, String>> companiesAutoFillInputIdsByFieldName = new LinkedList<>();
        List<Map<String, String>> employersAutoFillInputIdsByFieldName = new LinkedList<>();
        
        companiesAutoFillInputIdsByFieldName.add(companyAutoFillInputIdsByFieldName);

        autoFillConcernedEntities.add(Company.class.getSimpleName());

        legalDocument = new LegalDocument();
        legalDocument.setId("5dad0d7dd1a76c479c02affa937");
        legalDocument.setShortName("Convocation ago");
        legalDocument.setAutoFillConcernedEntities(autoFillConcernedEntities);
        legalDocument.setFullName("Convocation Ago nomination de gérant");
        legalDocument.setKeywords("Convocation ago gérance");
        legalDocument.setDescription("Convocation ago");
        legalDocument.setTemplatePreviewPath("empty_preview");
        legalDocument.setTemplateFilePath("convocation_AGO_nomination_de_gérant_docx_model.docx");
        legalDocument.setStepperConfigFilePath("convocation_ago_docx_model_stepper_configuration.json");
        legalDocument.setWorkflowConfigFilePath("convocation_ago_docx_model_workflow_configuration.json");
        legalDocument.setCategoryId("empty_category");
        legalDocument.setLawyerId("empty_lawyer");
        legalDocument.setEmployersAutoFillInputIdsByFieldName(employersAutoFillInputIdsByFieldName);
        legalDocument.setCompaniesAutoFillInputIdsByFieldName(companiesAutoFillInputIdsByFieldName);
        legalDocument.setCreatedDate(Instant.now());
        descriptionSection = new DescriptionSection();
        
       
      
        descriptionSection.setTitle("Convocation Ago:");
        descriptionSection.setContent("Procédure 100% en ligne pour créer votre Convocation Ago en toute simplicité.\n" +
            "Statuts générés en 10mn.");
        descriptionSections.add(descriptionSection);

        legalDocument.setDescriptionSections(descriptionSections);

        legalDocument.setPrice(49.99f);

        legalDocument.setDocumentsRecommendationId(new ArrayList<>());

        mongoTemplate.save(legalDocument);
    }
    
    @ChangeSet(order = "14", author = AuthorsConstants.FAHMI_BOUMAIZA , id = "14-addapprobationdescomptesLegalDocument")

    public void addapprobationdescomptesLegalDocument(MongoTemplate mongoTemplate) {     
        LegalDocument legalDocument;

        List<DescriptionSection> descriptionSections = new ArrayList<>();
        DescriptionSection descriptionSection;
        List<String> autoFillConcernedEntities = new ArrayList<>();
        Map<String, String> companyAutoFillInputIdsByFieldName = new LinkedHashMap<>();
        Map<String, String> employerAutoFillInputIdsByFieldName = new LinkedHashMap<>();
        List<Map<String, String>> companiesAutoFillInputIdsByFieldName = new LinkedList<>();
        List<Map<String, String>> employersAutoFillInputIdsByFieldName = new LinkedList<>();
        
        companiesAutoFillInputIdsByFieldName.add(companyAutoFillInputIdsByFieldName);

        autoFillConcernedEntities.add(Company.class.getSimpleName());

        legalDocument = new LegalDocument();
        legalDocument.setId("5dad0d7dd1a76c479c02affa939");
        legalDocument.setShortName("Approbation des comptes");
        legalDocument.setAutoFillConcernedEntities(autoFillConcernedEntities);
        legalDocument.setFullName("Approbation des comptes");
        legalDocument.setKeywords("Approbation des comptes   PV d’approbation de comptes");
        legalDocument.setDescription("Approbation des comptes");
        legalDocument.setTemplatePreviewPath("empty_preview");
        legalDocument.setTemplateFilePath("PV_AGO_SARL_APPROBATION_DES_COMPTES.doc");
        legalDocument.setStepperConfigFilePath("approbation_des_comptes_docx_model_stepper_configuration.json");
        legalDocument.setWorkflowConfigFilePath("approbation_des_comptes_docx_model_workflow_configuration.json");
        legalDocument.setCategoryId("empty_category");
        legalDocument.setLawyerId("empty_lawyer");
        legalDocument.setEmployersAutoFillInputIdsByFieldName(employersAutoFillInputIdsByFieldName);
        legalDocument.setCompaniesAutoFillInputIdsByFieldName(companiesAutoFillInputIdsByFieldName);
        legalDocument.setCreatedDate(Instant.now());
        descriptionSection = new DescriptionSection();
        
       
      
        descriptionSection.setTitle("Approbation des comptes:");
        descriptionSection.setContent("Procédure 100% en ligne pour créer votre Approbation des Comptes en toute simplicité.\n" +
            "Statuts générés en 10mn.");
        descriptionSections.add(descriptionSection);

        legalDocument.setDescriptionSections(descriptionSections);

        legalDocument.setPrice(49.99f);

        legalDocument.setDocumentsRecommendationId(new ArrayList<>());

        mongoTemplate.save(legalDocument);
    }
    
    @ChangeSet(order = "15", author = AuthorsConstants.BELHASSEN_ZINELABIDINE , id = "15-addAGONominationNouveauGerantLegalDocument")
    public void addAGONominationNouveauGerantLegalDocument(MongoTemplate mongoTemplate) {
        LegalDocument legalDocument;

        List<DescriptionSection> descriptionSections = new ArrayList<>();
        DescriptionSection descriptionSection;
        List<String> autoFillConcernedEntities = new ArrayList<>();
        Map<String, String> companyAutoFillInputIdsByFieldName = new LinkedHashMap<>();
        Map<String, String> employerAutoFillInputIdsByFieldName = new LinkedHashMap<>();
        List<Map<String, String>> companiesAutoFillInputIdsByFieldName = new LinkedList<>();
        List<Map<String, String>> employersAutoFillInputIdsByFieldName = new LinkedList<>();

        companiesAutoFillInputIdsByFieldName.add(companyAutoFillInputIdsByFieldName);
        employersAutoFillInputIdsByFieldName.add(employerAutoFillInputIdsByFieldName);

        autoFillConcernedEntities.add(Company.class.getSimpleName());
        autoFillConcernedEntities.add(Employer.class.getSimpleName());

        legalDocument = new LegalDocument();
        legalDocument.setId("5dde43ffd02a3b36803e18b88");
        legalDocument.setShortName("AGO-NNG");
        legalDocument.setAutoFillConcernedEntities(autoFillConcernedEntities);
        legalDocument.setFullName("AGO : Nomination d'un Nouveau Gérant");
        legalDocument.setKeywords("AGO nng gérance");
        legalDocument.setDescription("AGO : NOMINATION D'UN NOUVEAU GÉRANT");
        legalDocument.setTemplatePreviewPath("empty_preview");
        legalDocument.setTemplateFilePath("AGO_nomination d'un  nouveau gérant .docx");
        legalDocument.setStepperConfigFilePath("ago_nomination_nouveau_gerant_stepper_configuration.json");
        legalDocument.setWorkflowConfigFilePath("ago_nomination_nouveau_gerant_workflow_configuration.json");
        legalDocument.setCategoryId("empty_category");
        legalDocument.setLawyerId("empty_lawyer");
        legalDocument.setEmployersAutoFillInputIdsByFieldName(employersAutoFillInputIdsByFieldName);
        legalDocument.setCompaniesAutoFillInputIdsByFieldName(companiesAutoFillInputIdsByFieldName);
        legalDocument.setCreatedDate(Instant.now());

        descriptionSection = new DescriptionSection();
        descriptionSection.setTitle("Création de Procès-Verbal :AGO Nomination d'un Nouveau Gérant :");
        descriptionSection.setContent("Procédure 100% en ligne pour créer votre document en toute simplicité.");
        descriptionSections.add(descriptionSection);

        legalDocument.setDescriptionSections(descriptionSections);

        legalDocument.setPrice(10.59f);

        List<String> documentsRecommendationId = new ArrayList<>();
        legalDocument.setDocumentsRecommendationId(documentsRecommendationId);

        mongoTemplate.save(legalDocument);

    }
   
    
      
    
    @ChangeSet(order = "16", author = AuthorsConstants.FAHMI_BOUMAIZA , id = "16-addLettreDeDemissionGerantLegalDocument")
    public void addLettreDeDemissionGerantLegalDocument(MongoTemplate mongoTemplate) {     
        LegalDocument legalDocument;

        List<DescriptionSection> descriptionSections = new ArrayList<>();
        DescriptionSection descriptionSection;
        List<String> autoFillConcernedEntities = new ArrayList<>();

        autoFillConcernedEntities.add(Company.class.getSimpleName());

        legalDocument = new LegalDocument();
        legalDocument.setId("5dad0d7dd1a76c479c02affa96");
        legalDocument.setShortName("LETTRE DE DEMISSION GERANT");
        legalDocument.setAutoFillConcernedEntities(autoFillConcernedEntities);
        legalDocument.setFullName("Lettre de demission GERANT");
        legalDocument.setKeywords("lettre de demission gérance  PV de nomination du gérant");
        legalDocument.setDescription("MODÈLE LETTRE DE DEMISSION");
        legalDocument.setTemplatePreviewPath("empty_preview");
        legalDocument.setTemplateFilePath("lettre_de_démission_gérant.docx");
        legalDocument.setStepperConfigFilePath("lettre_de_demission_gerant_stepper_configuration.json");
        legalDocument.setWorkflowConfigFilePath("lettre_de_demission_gerant_workflow_configuration.json");
        legalDocument.setCategoryId("empty_category");
        legalDocument.setLawyerId("empty_lawyer");
        legalDocument.setCreatedDate(Instant.now());
      
        descriptionSection = new DescriptionSection();
        descriptionSection.setTitle("Création de LETTRE DE DEMISSION GERANT:");
        descriptionSection.setContent("Procédure 100% en ligne pour créer votre LETTRE DE DEMISSION GERANT en toute simplicité.\n" +
            "Statuts générés en 10mn.");
        descriptionSections.add(descriptionSection);    

        legalDocument.setDescriptionSections(descriptionSections);

        legalDocument.setPrice(49.99f);

        legalDocument.setDocumentsRecommendationId(new ArrayList<>());

        mongoTemplate.save(legalDocument);
    } 
    
    

 
         

}
