import { PaymentMethod } from 'app/shared/model/enumerations/payment-method.model';
import { OrderStatus } from 'app/shared/model/enumerations/order-status.model';
import { LegalDocument } from 'app/shared/model/legal-document.model';
import { DowloadHistory } from 'app/shared/model/dowload-history.model';
import { GeneratedLegalDocument } from 'app/shared/model/generated-legal-document.model';
import { User } from 'app/core/user/user.model';

export interface IOrder {
  id?: string;
  paymentMethod?: PaymentMethod;
  totalPrice?: number;
  price?: number;
  tax?: number;
  invoiceFilePath?: string;
  status?: OrderStatus;
  destinationEmail?: string;
  legalDocument?: LegalDocument;
  dowloadHistories?: DowloadHistory[];
  generatedLegalDocument?: GeneratedLegalDocument;
  user?: User;
  orderIdentifier?: string;
}

export class Order implements IOrder {
  constructor(
    public id?: string,
    public paymentMethod?: PaymentMethod,
    public totalPrice?: number,
    public price?: number,
    public tax?: number,
    public invoiceFilePath?: string,
    public status?: OrderStatus,
    public destinationEmail?: string,
    public legalDocument?: LegalDocument,
    public dowloadHistories?: DowloadHistory[],
    public generatedLegalDocument?: GeneratedLegalDocument,
    public user?: User,
    public orderIdentifier?: string
  ) {
    this.price = 0;
    this.totalPrice = 0;
    this.legalDocument = new LegalDocument();
    this.destinationEmail = '';
    this.dowloadHistories = [];
    this.status = OrderStatus.WAITING;
    this.generatedLegalDocument = new GeneratedLegalDocument();
    this.invoiceFilePath = '';
    this.paymentMethod = PaymentMethod.EDINAR;
    this.tax = 0;
    this.user = new User();
    this.orderIdentifier = '';
  }
}
