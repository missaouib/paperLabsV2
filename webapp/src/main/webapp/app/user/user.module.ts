import { NgModule } from '@angular/core';
import { LegalDocumentDescriptionComponent } from './legal-document-description/legal-document-description.component';
import { RouterModule } from '@angular/router';
import { PaperlabsSharedModule } from 'app/shared/shared.module';
import { ContactComponent } from 'app/user/contact/contact.component';
import { HowItWorksComponent } from 'app/user/how-it-works/how-it-works.component';
import { userRoutes } from 'app/user/user.route';
import { TeamComponent } from './team/team.component';
import { TermsAndConditionsComponent } from './terms-and-conditions/terms-and-conditions.component';
import { LegalNoticeComponent } from './legal-notice/legal-notice.component';
import { PrivacyPolicyComponent } from './privacy-policy/privacy-policy.component';
import { DocumentsAndLegalServicesComponent } from './documents-and-legal-services/documents-and-legal-services.component';
import { QuestionForLawyerComponent } from './question-for-lawyer/question-for-lawyer.component';
import { LegalGuidesComponent } from './legal-guides/legal-guides.component';
import { FaqComponent } from './faq/faq.component';
import { LawyersCharterComponent } from './lawyers-charter/lawyers-charter.component';
import { BusinessCreationComponent } from './business-creation/business-creation.component';
import { AssociationCreationComponent } from './association-creation/association-creation.component';
import { CommercialContractsComponent } from './commercial-contracts/commercial-contracts.component';
import { LeaseContractsComponent } from './lease-contracts/lease-contracts.component';
import { EmploymentContractsComponent } from './employment-contracts/employment-contracts.component';
import { ProfessionalSolutionComponent } from './professional-solution/professional-solution.component';
import { ContactExpertFormComponent } from './professional-solution/contact-expert-form/contact-expert-form.component';

@NgModule({
  declarations: [
    HowItWorksComponent,
    ContactComponent,
    LegalDocumentDescriptionComponent,
    TeamComponent,
    TermsAndConditionsComponent,
    LegalNoticeComponent,
    PrivacyPolicyComponent,
    DocumentsAndLegalServicesComponent,
    QuestionForLawyerComponent,
    LegalGuidesComponent,
    FaqComponent,
    LawyersCharterComponent,
    BusinessCreationComponent,
    AssociationCreationComponent,
    CommercialContractsComponent,
    LeaseContractsComponent,
    EmploymentContractsComponent,
    ProfessionalSolutionComponent,
    ContactExpertFormComponent
  ],
  imports: [PaperlabsSharedModule, RouterModule.forChild(userRoutes)]
})
export class UserModule {}
