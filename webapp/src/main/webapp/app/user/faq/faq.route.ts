import { Route } from '@angular/router';
import { FaqComponent } from 'app/user/faq/faq.component';

export const faqRoute: Route = {
  path: 'faq',
  component: FaqComponent,
  data: {
    pageTitle: 'paperlabsApp.faq.title'
  }
};
