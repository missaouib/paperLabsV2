import { Routes } from '@angular/router';
import { StepperComponent } from 'app/stepper/stepper/stepper.component';
import { GenerateDocumentComponent } from 'app/stepper/generate-document/generate-document.component';
import { LegalDocumentResolve } from 'app/entities/legal-document/legal-document.route';

export const stepperRoutes: Routes = [
  {
    path: 'doc/:id',
    component: StepperComponent,
    resolve: {
      legalDocument: LegalDocumentResolve
    },
    data: {
      pageTitle: 'stepper.title'
    }
  },
  {
    path: 'generate-document',
    component: GenerateDocumentComponent,
    data: {
      pageTitle: 'generate-document.title'
    }
  }
];
