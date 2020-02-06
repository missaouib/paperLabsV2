import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PaperlabsSharedModule } from 'app/shared/shared.module';
import { GeneratedLegalDocumentComponent } from './generated-legal-document.component';
import { GeneratedLegalDocumentDetailComponent } from './generated-legal-document-detail.component';
import { GeneratedLegalDocumentUpdateComponent } from './generated-legal-document-update.component';
import {
  GeneratedLegalDocumentDeletePopupComponent,
  GeneratedLegalDocumentDeleteDialogComponent
} from './generated-legal-document-delete-dialog.component';
import { generatedLegalDocumentRoute, generatedLegalDocumentPopupRoute } from './generated-legal-document.route';

const ENTITY_STATES = [...generatedLegalDocumentRoute, ...generatedLegalDocumentPopupRoute];

@NgModule({
  imports: [PaperlabsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    GeneratedLegalDocumentComponent,
    GeneratedLegalDocumentDetailComponent,
    GeneratedLegalDocumentUpdateComponent,
    GeneratedLegalDocumentDeleteDialogComponent,
    GeneratedLegalDocumentDeletePopupComponent
  ],
  entryComponents: [GeneratedLegalDocumentDeleteDialogComponent]
})
export class PaperlabsGeneratedLegalDocumentModule {}
