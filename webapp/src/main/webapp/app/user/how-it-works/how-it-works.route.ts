import { Route } from '@angular/router';
import { HowItWorksComponent } from 'app/user/how-it-works/how-it-works.component';

export const howItWorksRoute: Route = {
  path: 'how-it-works',
  component: HowItWorksComponent,
  data: {
    pageTitle: 'paperlabsApp.how-it-works.title'
  }
};
