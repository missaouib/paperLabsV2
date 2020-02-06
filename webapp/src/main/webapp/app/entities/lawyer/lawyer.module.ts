import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PaperlabsSharedModule } from 'app/shared/shared.module';
import { LawyerComponent } from './lawyer.component';
import { LawyerDetailComponent } from './lawyer-detail.component';
import { LawyerUpdateComponent } from './lawyer-update.component';
import { LawyerDeletePopupComponent, LawyerDeleteDialogComponent } from './lawyer-delete-dialog.component';
import { lawyerRoute, lawyerPopupRoute } from './lawyer.route';

const ENTITY_STATES = [...lawyerRoute, ...lawyerPopupRoute];

@NgModule({
  imports: [PaperlabsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [LawyerComponent, LawyerDetailComponent, LawyerUpdateComponent, LawyerDeleteDialogComponent, LawyerDeletePopupComponent],
  entryComponents: [LawyerDeleteDialogComponent]
})
export class PaperlabsLawyerModule {}
