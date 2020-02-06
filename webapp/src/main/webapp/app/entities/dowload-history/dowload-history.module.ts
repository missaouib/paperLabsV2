import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PaperlabsSharedModule } from 'app/shared/shared.module';
import { DowloadHistoryComponent } from './dowload-history.component';
import { DowloadHistoryDetailComponent } from './dowload-history-detail.component';
import { DowloadHistoryUpdateComponent } from './dowload-history-update.component';
import { DowloadHistoryDeletePopupComponent, DowloadHistoryDeleteDialogComponent } from './dowload-history-delete-dialog.component';
import { dowloadHistoryRoute, dowloadHistoryPopupRoute } from './dowload-history.route';

const ENTITY_STATES = [...dowloadHistoryRoute, ...dowloadHistoryPopupRoute];

@NgModule({
  imports: [PaperlabsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    DowloadHistoryComponent,
    DowloadHistoryDetailComponent,
    DowloadHistoryUpdateComponent,
    DowloadHistoryDeleteDialogComponent,
    DowloadHistoryDeletePopupComponent
  ],
  entryComponents: [DowloadHistoryDeleteDialogComponent]
})
export class PaperlabsDowloadHistoryModule {}
