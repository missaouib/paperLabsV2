import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { IOrder } from 'app/shared/model/order.model';

@Injectable({ providedIn: 'root' })
export class StepperEventManagerService {
  private generateDocument = new Subject<any>();
  private nextPrevSubject = new Subject<any>();
  private onChangeSubject = new Subject<string>();
  private orderSubject = new Subject<IOrder>();

  constructor() {}

  sendGenerateDocument(stepperData: any, legalDocumentId: string, orderId: string) {
    this.generateDocument.next({ stepperData, legalDocumentId, orderId });
  }

  getGenerateDocument(): Observable<any> {
    return this.generateDocument.asObservable();
  }

  sendNextPrev(currentStepId: string, action: number, orderId: string) {
    this.nextPrevSubject.next({ currentStepId, action, orderId });
  }

  getNextPrev(): Observable<any> {
    return this.nextPrevSubject.asObservable();
  }

  sendOnChange(inputId: string) {
    this.onChangeSubject.next(inputId);
  }

  getOnChange(): Observable<string> {
    return this.onChangeSubject.asObservable();
  }

  sendOrder(order: IOrder) {
    this.orderSubject.next(order);
  }

  getOrder(): Observable<IOrder> {
    return this.orderSubject.asObservable();
  }
}
