import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PaperlabsSharedModule } from 'app/shared/shared.module';
import { EmployerComponent } from './employer.component';
import { EmployerDetailComponent } from './employer-detail.component';
import { EmployerUpdateComponent } from './employer-update.component';
import { EmployerDeletePopupComponent, EmployerDeleteDialogComponent } from './employer-delete-dialog.component';
import { employerRoute, employerPopupRoute } from './employer.route';

const ENTITY_STATES = [...employerRoute, ...employerPopupRoute];

@NgModule({
  imports: [PaperlabsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    EmployerComponent,
    EmployerDetailComponent,
    EmployerUpdateComponent,
    EmployerDeleteDialogComponent,
    EmployerDeletePopupComponent
  ],
  entryComponents: [EmployerDeleteDialogComponent]
})
export class PaperlabsEmployerModule {}
