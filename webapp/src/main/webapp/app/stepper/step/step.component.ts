import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AccountService } from 'app/core/auth/account.service';
import { StepEntity } from 'app/entities/StepEntity';
import { StepperEventManagerService } from 'app/stepper/stepper/stepper-event-manager.service';
import { IOrder } from 'app/shared/model/order.model';
import { PreviewModalService } from 'app/stepper/preview/preview.service';
import { LegalDocument } from 'app/shared/model/legal-document.model';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'jhi-step',
  templateUrl: './step.component.html',
  styleUrls: ['step.scss']
})
export class StepComponent implements OnInit {
  @Input() step: StepEntity;
  @Input() isFirstStep: boolean;
  @Input() isFinalStep: boolean;
  @Input() isConfirmGenerationStep: boolean;
  @Input() legalDocument: LegalDocument;
  @Output() downloadPDFEvent = new EventEmitter<IOrder>();
  orderIsPurchased = false;
  order: IOrder;

  fileUrl="['user/legal-document-description/', legalDocument.id]" ;

  constructor(
    private sanitizer: DomSanitizer,
    private accountService: AccountService,
    private stepperEventManager: StepperEventManagerService,
    private previewModalService: PreviewModalService
  ) {}

  ngOnInit() {
   /* const data = 'some text';
    const blob = new Blob([data], { type: 'application/octet-stream' });

    this.fileUrl = this.sanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(blob));*/


  }
  

  isAuthenticated() {
    return this.accountService.isAuthenticated();
  }

  hasPreview(): boolean {
    
    if (this.legalDocument !== null && this.legalDocument !== undefined) {
      return 'CDI' !== this.legalDocument.shortName;
    } else {
      return false;
    }
  }

  displayModal() {
    this.previewModalService.open();
  }

  nextPrev(currentStepId: string, action: number, orderId: string) {
    this.stepperEventManager.sendNextPrev(currentStepId, action, orderId);
  }

  purchase(order: IOrder) {
    this.order = order;
    this.orderIsPurchased = true;
  }
}
