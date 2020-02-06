import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PaperlabsTestModule } from '../../../test.module';
import { GeneratedLegalDocumentDetailComponent } from 'app/entities/generated-legal-document/generated-legal-document-detail.component';
import { GeneratedLegalDocument } from 'app/shared/model/generated-legal-document.model';

describe('Component Tests', () => {
  describe('GeneratedLegalDocument Management Detail Component', () => {
    let comp: GeneratedLegalDocumentDetailComponent;
    let fixture: ComponentFixture<GeneratedLegalDocumentDetailComponent>;
    const route = ({ data: of({ generatedLegalDocument: new GeneratedLegalDocument('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PaperlabsTestModule],
        declarations: [GeneratedLegalDocumentDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(GeneratedLegalDocumentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GeneratedLegalDocumentDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.generatedLegalDocument).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
