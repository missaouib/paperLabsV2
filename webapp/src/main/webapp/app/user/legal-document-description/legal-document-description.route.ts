import { Route } from '@angular/router';
import { LegalDocumentDescriptionComponent } from 'app/user/legal-document-description/legal-document-description.component';
import { LegalDocumentResolve } from 'app/entities/legal-document/legal-document.route';

export const legalDocumentDescriptionRoute: Route = {
  path: 'legal-document-description/:id',
  component: LegalDocumentDescriptionComponent,
  resolve: {
    legalDocument: LegalDocumentResolve
  },
  data: {
    pageTitle: 'paperlabsApp.legalDocumentDescription.title'
  }
};
