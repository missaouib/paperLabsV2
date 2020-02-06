import { Route } from '@angular/router';
import { EmploymentContractsComponent } from 'app/user/employment-contracts/employment-contracts.component';

export const employmentContractsRoute: Route = {
  path: 'employment-contracts',
  component: EmploymentContractsComponent,
  data: {
    pageTitle: 'paperlabsApp.employmentContracts.title'
  }
};
