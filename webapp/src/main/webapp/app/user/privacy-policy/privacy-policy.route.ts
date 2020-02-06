import { Route } from '@angular/router';
import { PrivacyPolicyComponent } from 'app/user/privacy-policy/privacy-policy.component';

export const privacyPolicyRoute: Route = {
  path: 'privacy-policy',
  component: PrivacyPolicyComponent,
  data: {
    pageTitle: 'paperlabsApp.privacyPolicy.title'
  }
};
