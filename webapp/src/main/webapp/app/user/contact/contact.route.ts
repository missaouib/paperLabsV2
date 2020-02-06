import { Route } from '@angular/router';
import { ContactComponent } from 'app/user/contact/contact.component';

export const contactRoute: Route = {
  path: 'contact',
  component: ContactComponent,
  data: {
    pageTitle: 'contact.title'
  }
};
