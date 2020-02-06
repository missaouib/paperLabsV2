import { Route } from '@angular/router';
import { TeamComponent } from 'app/user/team/team.component';

export const teamRoute: Route = {
  path: 'team',
  component: TeamComponent,
  data: {
    pageTitle: 'paperlabsApp.team.title'
  }
};
