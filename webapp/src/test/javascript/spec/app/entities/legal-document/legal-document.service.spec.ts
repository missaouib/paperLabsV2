import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import { LegalDocumentService } from 'app/entities/legal-document/legal-document.service';
import { ILegalDocument, LegalDocument } from 'app/shared/model/legal-document.model';

describe('Service Tests', () => {
  describe('LegalDocument Service', () => {
    let injector: TestBed;
    let service: LegalDocumentService;
    let httpMock: HttpTestingController;
    let elemDefault: ILegalDocument;
    let expectedResult;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(LegalDocumentService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new LegalDocument(
        'ID',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        null,
        0
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);
        service
          .find('123')
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a LegalDocument', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID'
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        service
          .create(new LegalDocument(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a LegalDocument', () => {
        const returnedFromService = Object.assign(
          {
            shortName: 'BBBBBB',
            fullName: 'BBBBBB',
            keywords: 'BBBBBB',
            description: 'BBBBBB',
            templatePreviewPath: 'BBBBBB',
            templateFilePath: 'BBBBBB',
            stepperConfigFilePath: 'BBBBBB',
            workflowConfigFilePath: 'BBBBBB',
            price: 1
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of LegalDocument', () => {
        const returnedFromService = Object.assign(
          {
            shortName: 'BBBBBB',
            fullName: 'BBBBBB',
            keywords: 'BBBBBB',
            description: 'BBBBBB',
            templatePreviewPath: 'BBBBBB',
            templateFilePath: 'BBBBBB',
            stepperConfigFilePath: 'BBBBBB',
            workflowConfigFilePath: 'BBBBBB',
            price: 1
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a LegalDocument', () => {
        service.delete('123').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
