import { Route } from '@angular/router';
import { LeaseContractsComponent } from 'app/user/lease-contracts/lease-contracts.component';

export const leaseContractsRoute: Route = {
  path: 'lease-contracts',
  component: LeaseContractsComponent,
  data: {
    pageTitle: 'paperlabsApp.leaseContracts.title'
  }
};
