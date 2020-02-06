import { Route } from '@angular/router';
import { BusinessCreationComponent } from 'app/user/business-creation/business-creation.component';

export const businessCreationRoute: Route = {
  path: 'business-creation',
  component: BusinessCreationComponent,
  data: {
    pageTitle: 'paperlabsApp.businessCreation.title'
  }
};
