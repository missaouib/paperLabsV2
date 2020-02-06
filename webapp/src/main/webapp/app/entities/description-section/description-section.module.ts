import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PaperlabsSharedModule } from 'app/shared/shared.module';
import { DescriptionSectionComponent } from './description-section.component';
import { DescriptionSectionDetailComponent } from './description-section-detail.component';
import { DescriptionSectionUpdateComponent } from './description-section-update.component';
import {
  DescriptionSectionDeletePopupComponent,
  DescriptionSectionDeleteDialogComponent
} from './description-section-delete-dialog.component';
import { descriptionSectionRoute, descriptionSectionPopupRoute } from './description-section.route';

const ENTITY_STATES = [...descriptionSectionRoute, ...descriptionSectionPopupRoute];

@NgModule({
  imports: [PaperlabsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    DescriptionSectionComponent,
    DescriptionSectionDetailComponent,
    DescriptionSectionUpdateComponent,
    DescriptionSectionDeleteDialogComponent,
    DescriptionSectionDeletePopupComponent
  ],
  entryComponents: [DescriptionSectionDeleteDialogComponent]
})
export class PaperlabsDescriptionSectionModule {}
