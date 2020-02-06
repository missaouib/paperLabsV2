import { Route } from '@angular/router';
import { LegalGuidesComponent } from 'app/user/legal-guides/legal-guides.component';

export const legalGuidesRoute: Route = {
  path: 'legal-guides',
  component: LegalGuidesComponent,
  data: {
    pageTitle: 'paperlabsApp.legalGuides.title'
  }
};
