import { Route } from '@angular/router';
import { TermsAndConditionsComponent } from 'app/user/terms-and-conditions/terms-and-conditions.component';

export const termsAndConditionsRoute: Route = {
  path: 'terms-and-conditions',
  component: TermsAndConditionsComponent,
  data: {
    pageTitle: 'paperlabsApp.termsAndConditions.title'
  }
};
