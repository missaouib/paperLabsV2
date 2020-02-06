import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse, HttpUrlEncodingCodec } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';
import { StepperConfig } from 'app/entities/StepperConfig';
import { Constraint } from 'app/entities/Constraint';
import { IOrder } from 'app/shared/model/order.model';

type StepperConfigResponseType = HttpResponse<StepperConfig>;
type ConstraintArrayResponseType = HttpResponse<Constraint[]>;
type CompanyTaxStampResponseType = HttpResponse<number>;

@Injectable({ providedIn: 'root' })
export class StepperApiService {
  public stepperConfigResourceUrl = SERVER_API_URL + 'api/stepper';
  public inputIdsWithConstraintsResourceUrl = SERVER_API_URL + 'api/inputids-with-constraints';
  public constraintsResourceUrl = SERVER_API_URL + 'api/constraints';
  public generateDocXFileResourceUrl = SERVER_API_URL + 'api/generateDocXFile';
  public generateAndSendThePdfDocumentResourceUrl = SERVER_API_URL + 'api/report/generate-sendPdfFile';
  public generateAndSendTheInvoiceResourceUrl = SERVER_API_URL + 'api/report/generate-sendInvoiceFile';
  public downloadPdfFileResourceUrl = SERVER_API_URL + 'api/downloadPdfFile';
  public getCompanyTaxStampResourceUrl = SERVER_API_URL + 'api/legalDocumentTaxStamp';

  public httpUrlEncodingCodec: HttpUrlEncodingCodec;

  constructor(protected http: HttpClient) {
    this.httpUrlEncodingCodec = new HttpUrlEncodingCodec();
  }

  getStepperConfig(legalDocumentId: string): Observable<StepperConfigResponseType> {
    return this.http.get<StepperConfig>(`${this.stepperConfigResourceUrl}?legalDocumentId=${legalDocumentId}`, { observe: 'response' });
  }

  getInputIdsWithConstraints(legalDocumentId: string): Observable<HttpResponse<string[]>> {
    return this.http.get<string[]>(`${this.inputIdsWithConstraintsResourceUrl}?legalDocumentId=${legalDocumentId}`, {
      observe: 'response'
    });
  }

  getConstraints(inputId: string, value: string, legalDocumentId: string): Observable<ConstraintArrayResponseType> {
    return this.http.get<Constraint[]>(
      `${this.constraintsResourceUrl}?inputId=${inputId}&legalDocumentId=${legalDocumentId}&value=${value}`,
      {
        observe: 'response'
      }
    );
  }

  generateDocXFile(
    stepperDataDTO: any,
    legalDocumentId: string,
    saveDataAuthorization: string,
    userLogin: string,
    orderId: string
  ): Observable<HttpResponse<IOrder>> {
    return this.http.post<IOrder>(
      `${this.generateDocXFileResourceUrl}?legalDocumentId=${legalDocumentId}&saveDataAuthorization=${saveDataAuthorization}&userLogin=${userLogin}&orderId=${orderId}`,
      stepperDataDTO,
      { observe: 'response' }
    );
  }

  generateAndSendThePdfDocument(orderId: string): Observable<HttpResponse<IOrder>> {
    return this.http.get<IOrder>(`${this.generateAndSendThePdfDocumentResourceUrl}/${orderId}`, { observe: 'response' });
  }

  generateAndSendTheInvoice(orderId: string): Observable<HttpResponse<IOrder>> {
    return this.http.get<IOrder>(`${this.generateAndSendTheInvoiceResourceUrl}/${orderId}`, { observe: 'response' });
  }

  downloadPdfFile(orderId: string, filePath: string): Observable<Blob> {
    let headers = new HttpHeaders();
    headers = headers.append('Accept', 'application/pdf; charset=utf-8');

    return this.http.get(
      `${this.downloadPdfFileResourceUrl}?filePath=${this.httpUrlEncodingCodec.encodeValue(filePath)}&orderId=${orderId}`,
      {
        headers,
        responseType: 'blob'
      }
    );
  }

  getCompanyTaxStamp(): Observable<CompanyTaxStampResponseType> {
    return this.http.get<number>(`${this.getCompanyTaxStampResourceUrl}`, { observe: 'response' });
  }
}
