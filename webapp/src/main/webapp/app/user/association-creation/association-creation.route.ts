import { Route } from '@angular/router';
import { AssociationCreationComponent } from 'app/user/association-creation/association-creation.component';

export const associationCreationRoute: Route = {
  path: 'association-creation',
  component: AssociationCreationComponent,
  data: {
    pageTitle: 'paperlabsApp.associationCreation.title'
  }
};
