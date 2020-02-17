import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AccountService } from 'app/core/auth/account.service';
import { StepEntity } from 'app/entities/StepEntity';
import { StepperEventManagerService } from 'app/stepper/stepper/stepper-event-manager.service';
import { IOrder } from 'app/shared/model/order.model';
import { PreviewModalService } from 'app/stepper/preview/preview.service';
import { LegalDocument } from 'app/shared/model/legal-document.model';
import { DomSanitizer } from '@angular/platform-browser';

import { DatePipe } from '@angular/common';
/* import fs = require("fs");
import { HttpClient } from "typed-rest-client/HttpClient";
 */


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


  // fileUrl="\\model_docx_1.docx\\" ;

  date = this.datePipe.transform(new Date(), 'yyyyMMddHHmm');
  fileUrl = 'folder/docx_filled_' + this.date + '.docx';


  constructor(
    private sanitizer: DomSanitizer,
    private accountService: AccountService,
    private stepperEventManager: StepperEventManagerService,
    private previewModalService: PreviewModalService
  ) {}

  ngOnInit() {

  

    // const date = this.datePipe.transform(new Date(), 'yyyymmddhhmmss');
    // this.fileUrl='docx_filled_'+date+'.docx'
  }

  download() {
    // alert('***************************************************************')
    // location.href = 'folder/test.txt';
    //  location.href ='folder/docx_filled_'+this.date+'.docx';
    /* const element = document.createElement('a');
              element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent('aa'));
              element.setAttribute('download', 'foler/test.txt');
 
              element.style.display = 'none';
               document.body.appendChild(element);

              element.click();

                 document.body.removeChild(element); */
    /* const client = new HttpClient("clientTest");
              const response =  client.get("http://localhost:8080/folder/test.txt");
              const filePath = "waw.txt'";

              const file: NodeJS.WritableStream = fs.createWriteStream(filePath);
              
              if (response.message.statusCode !== 200) {
                  const err: Error = new Error(`Unexpected HTTP response: ${response.message.statusCode}`);
                  err["httpStatusCode"] = response.message.statusCode;
                  throw err;
              }
              
              return new Promise((resolve, reject) => {
                  file.on("error", (err) => reject(err));
                  const stream = response.message.pipe(file);
                  stream.on("close", () => {
                      try { resolve(filePath); } catch (err) {
                          reject(err);
                      }
                  });
              }); */
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
