import { Routes } from '@angular/router';
import { legalDocumentDescriptionRoute } from 'app/user/legal-document-description/legal-document-description.route';
import { howItWorksRoute } from 'app/user/how-it-works/how-it-works.route';
import { contactRoute } from 'app/user/contact/contact.route';
import { associationCreationRoute } from 'app/user/association-creation/association-creation.route';
import { businessCreationRoute } from 'app/user/business-creation/business-creation.route';
import { commercialContractsRoute } from 'app/user/commercial-contracts/commercial-contracts.route';
import { documentsAndLegalServicesRoute } from 'app/user/documents-and-legal-services/documents-and-legal-services.route';
import { employmentContractsRoute } from 'app/user/employment-contracts/employment-contracts.route';
import { faqRoute } from 'app/user/faq/faq.route';
import { lawyersCharterRoute } from 'app/user/lawyers-charter/lawyers-charter.route';
import { leaseContractsRoute } from 'app/user/lease-contracts/lease-contracts.route';
import { legalGuidesRoute } from 'app/user/legal-guides/legal-guides.route';
import { legalNoticeRoute } from 'app/user/legal-notice/legal-notice.route';
import { privacyPolicyRoute } from 'app/user/privacy-policy/privacy-policy.route';
import { questionForLawyerRoute } from 'app/user/question-for-lawyer/question-for-lawyer.route';
import { teamRoute } from 'app/user/team/team.route';
import { termsAndConditionsRoute } from 'app/user/terms-and-conditions/terms-and-conditions.route';
import { professionalSolutionRoute } from 'app/user/professional-solution/professional-solution.route';

const USER_ROUTES = [
  howItWorksRoute,
  contactRoute,
  legalDocumentDescriptionRoute,
  associationCreationRoute,
  businessCreationRoute,
  commercialContractsRoute,
  documentsAndLegalServicesRoute,
  employmentContractsRoute,
  faqRoute,
  lawyersCharterRoute,
  leaseContractsRoute,
  legalGuidesRoute,
  legalNoticeRoute,
  privacyPolicyRoute,
  questionForLawyerRoute,
  teamRoute,
  termsAndConditionsRoute,
  professionalSolutionRoute
];

export const userRoutes: Routes = [
  {
    path: '',
    children: USER_ROUTES
  }
];
