import { Route } from '@angular/router';
import { CommercialContractsComponent } from 'app/user/commercial-contracts/commercial-contracts.component';

export const commercialContractsRoute: Route = {
  path: 'commercial-contracts',
  component: CommercialContractsComponent,
  data: {
    pageTitle: 'paperlabsApp.commercialContracts.title'
  }
};
