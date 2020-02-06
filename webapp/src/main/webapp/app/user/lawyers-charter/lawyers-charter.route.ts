import { Route } from '@angular/router';
import { LawyersCharterComponent } from 'app/user/lawyers-charter/lawyers-charter.component';

export const lawyersCharterRoute: Route = {
  path: 'lawyers-charter',
  component: LawyersCharterComponent,
  data: {
    pageTitle: 'paperlabsApp.lawyersCharter.title'
  }
};
