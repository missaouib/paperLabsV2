import { NgModule } from '@angular/core';
import { PaperlabsSharedModule } from 'app/shared/shared.module';
import { EmployerComponent } from './employer/employer.component';
import { EmployersComponent } from './employers/employers.component';
import { CompanyComponent } from './company/company.component';
import { MatTabsModule, MatToolbarModule } from '@angular/material';
import { CompanyDetailComponent } from 'app/account/employers/company/company-detail.component';
import { EmployerDetailComponent } from 'app/account/employers/employer/employer-detail.component';
import { RouterModule } from '@angular/router';
import { employersRoute } from 'app/account/employers/employers.route';

@NgModule({
  declarations: [EmployerComponent, EmployersComponent, CompanyComponent, CompanyDetailComponent, EmployerDetailComponent],
  exports: [EmployerComponent, EmployersComponent, CompanyComponent, CompanyDetailComponent, EmployerDetailComponent],
  imports: [PaperlabsSharedModule, MatTabsModule, MatToolbarModule, RouterModule.forChild(employersRoute)]
})
export class EmployersModule {}
