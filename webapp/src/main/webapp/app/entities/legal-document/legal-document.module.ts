import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PaperlabsSharedModule } from 'app/shared/shared.module';
import { LegalDocumentComponent } from './legal-document.component';
import { LegalDocumentDetailComponent } from './legal-document-detail.component';
import { LegalDocumentUpdateComponent } from './legal-document-update.component';
import { LegalDocumentDeletePopupComponent, LegalDocumentDeleteDialogComponent } from './legal-document-delete-dialog.component';
import { legalDocumentRoute, legalDocumentPopupRoute } from './legal-document.route';

const ENTITY_STATES = [...legalDocumentRoute, ...legalDocumentPopupRoute];

@NgModule({
  imports: [PaperlabsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    LegalDocumentComponent,
    LegalDocumentDetailComponent,
    LegalDocumentUpdateComponent,
    LegalDocumentDeleteDialogComponent,
    LegalDocumentDeletePopupComponent
  ],
  entryComponents: [LegalDocumentDeleteDialogComponent]
})
export class PaperlabsLegalDocumentModule {}
