import { Route } from '@angular/router';
import { DocumentsAndLegalServicesComponent } from 'app/user/documents-and-legal-services/documents-and-legal-services.component';

export const documentsAndLegalServicesRoute: Route = {
  path: 'documents-and-legal-services',
  component: DocumentsAndLegalServicesComponent,
  data: {
    pageTitle: 'paperlabsApp.documentsAndLegalServices.title'
  }
};
