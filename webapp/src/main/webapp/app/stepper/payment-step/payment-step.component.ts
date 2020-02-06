import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
import { IOrder } from 'app/shared/model/order.model';
import { StepperEventManagerService } from 'app/stepper/stepper/stepper-event-manager.service';
import { Subscription } from 'rxjs';
import { FormBuilder, Validators } from '@angular/forms';
import { StepperApiService } from 'app/stepper/stepper/stepper-api.service';
import { OrderService } from 'app/entities/order/order.service';
import { ActivatedRoute } from '@angular/router';
import { StepperDataService } from 'app/stepper/stepper/stepper-data.service';
import { BsLocaleService, defineLocale, setTheme } from 'ngx-bootstrap';
import { frLocale } from 'ngx-bootstrap/locale';

@Component({
  selector: 'jhi-payment-step',
  templateUrl: './payment-step.component.html',
  styleUrls: ['./payment-step.component.scss', '../step/step.scss', '../generate-document/generate-document.component.scss']
})
export class PaymentStepComponent implements OnInit, OnDestroy {
  order: IOrder = {};
  orderSubscriber: Subscription;
  selectedPaymentOption = 'edinar';
  @Output() purchaseEvent = new EventEmitter<IOrder>();
  orderIsPurchased = false;
  purchaseConfirmed = false;
  invoiceIsConfirmed = false;
  orderTotalPrice: number;
  companyTaxStamp: number;

  edinarForm = this.formBuilder.group({
    cardNumber: ['', [Validators.required]],
    expirationDate: ['', [Validators.required]],
    cardVerificationCode: ['', [Validators.required]],
    conditions: [false, [Validators.pattern('true')]]
  });

  constructor(
    private localeService: BsLocaleService,
    private stepperDataService: StepperDataService,
    private stepperEventManagerService: StepperEventManagerService,
    private formBuilder: FormBuilder,
    private stepperApiService: StepperApiService,
    private orderService: OrderService,
    private route: ActivatedRoute
  ) {
    setTheme('bs4');
    frLocale.monthsShort = ['Jan', 'Fev', 'Mar', 'Avr', 'Mai', 'Juin', 'Juil', 'Aou', 'Sep', 'Oct', 'Nov', 'Dec'];
    defineLocale('fr', frLocale);
  }

  ngOnInit() {
    this.localeService.use('fr');
    const orderId = this.route.snapshot.paramMap.get('orderId');
    if (orderId !== null) {
      this.orderService.find(orderId).subscribe(res => {
        this.order = res.body;
        this.orderTotalPrice = res.body.totalPrice;
      });
    }
    this.orderSubscriber = this.stepperEventManagerService.getOrder().subscribe(order => {
      this.order = order;
      this.orderTotalPrice = order.totalPrice;
    });
    this.stepperApiService.getCompanyTaxStamp().subscribe(res => {
      this.companyTaxStamp = res.body;
    });
  }

  getTaxPrice() {
    return (this.order.price * this.order.tax) / 100;
  }

  paymentOptionIsSelected(value: string): boolean {
    return value === this.selectedPaymentOption;
  }

  confirmPurchase() {
    this.purchaseConfirmed = true;
    this.stepperApiService.generateAndSendThePdfDocument(this.order.id).subscribe(generatePDFResponse => {
      this.order = generatePDFResponse.body;
      this.purchaseEvent.emit(this.order);
      this.orderIsPurchased = true;
      this.purchaseConfirmed = false;
      if (this.invoiceIsConfirmed) {
        this.order.totalPrice = this.orderTotalPrice;
        this.orderService.update(this.order);
        this.stepperApiService.generateAndSendTheInvoice(this.order.id).subscribe();
      }
    });
  }

  getCompanyTaxStamp(): number {
    return this.companyTaxStamp;
  }

  upateTotalPrice() {
    this.invoiceIsConfirmed ? (this.orderTotalPrice += this.companyTaxStamp) : (this.orderTotalPrice -= this.companyTaxStamp);
  }

  ngOnDestroy(): void {
    this.orderSubscriber.unsubscribe();
  }

  downloadPDF() {
    this.stepperDataService.downloadPDF(this.order);
  }

  onOpenCalendar(container) {
    container.monthSelectHandler = (event: any): void => {
      container._store.dispatch(container._actions.select(event.date));
    };
    container.setViewMode('month');
  }
}
