import { Route } from '@angular/router';
import { ProfessionalSolutionComponent } from 'app/user/professional-solution/professional-solution.component';

export const professionalSolutionRoute: Route = {
  path: 'professional-solution',
  component: ProfessionalSolutionComponent,
  data: {
    pageTitle: 'paperlabsApp.professionalSolution.title'
  }
};
