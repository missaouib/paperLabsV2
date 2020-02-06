import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PaperlabsTestModule } from '../../../test.module';
import { LegalDocumentDetailComponent } from 'app/entities/legal-document/legal-document-detail.component';
import { LegalDocument } from 'app/shared/model/legal-document.model';

describe('Component Tests', () => {
  describe('LegalDocument Management Detail Component', () => {
    let comp: LegalDocumentDetailComponent;
    let fixture: ComponentFixture<LegalDocumentDetailComponent>;
    const route = ({ data: of({ legalDocument: new LegalDocument('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PaperlabsTestModule],
        declarations: [LegalDocumentDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(LegalDocumentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LegalDocumentDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.legalDocument).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
