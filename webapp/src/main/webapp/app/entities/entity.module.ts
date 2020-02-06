import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'category',
        loadChildren: () => import('./category/category.module').then(m => m.PaperlabsCategoryModule)
      },
      {
        path: 'legal-document',
        loadChildren: () => import('./legal-document/legal-document.module').then(m => m.PaperlabsLegalDocumentModule)
      },
      {
        path: 'generated-legal-document',
        loadChildren: () =>
          import('./generated-legal-document/generated-legal-document.module').then(m => m.PaperlabsGeneratedLegalDocumentModule)
      },
      {
        path: 'description-section',
        loadChildren: () => import('./description-section/description-section.module').then(m => m.PaperlabsDescriptionSectionModule)
      },
      {
        path: 'lawyer',
        loadChildren: () => import('./lawyer/lawyer.module').then(m => m.PaperlabsLawyerModule)
      },
      {
        path: 'order',
        loadChildren: () => import('./order/order.module').then(m => m.PaperlabsOrderModule)
      },
      {
        path: 'dowload-history',
        loadChildren: () => import('./dowload-history/dowload-history.module').then(m => m.PaperlabsDowloadHistoryModule)
      },
      {
        path: 'company',
        loadChildren: () => import('./company/company.module').then(m => m.PaperlabsCompanyModule)
      },
      {
        path: 'employee',
        loadChildren: () => import('./employee/employee.module').then(m => m.PaperlabsEmployeeModule)
      },
      {
        path: 'employer',
        loadChildren: () => import('./employer/employer.module').then(m => m.PaperlabsEmployerModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class PaperlabsEntityModule {}
