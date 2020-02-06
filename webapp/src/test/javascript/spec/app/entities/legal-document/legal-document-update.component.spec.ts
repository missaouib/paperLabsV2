import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PaperlabsTestModule } from '../../../test.module';
import { LegalDocumentUpdateComponent } from 'app/entities/legal-document/legal-document-update.component';
import { LegalDocumentService } from 'app/entities/legal-document/legal-document.service';
import { LegalDocument } from 'app/shared/model/legal-document.model';

describe('Component Tests', () => {
  describe('LegalDocument Management Update Component', () => {
    let comp: LegalDocumentUpdateComponent;
    let fixture: ComponentFixture<LegalDocumentUpdateComponent>;
    let service: LegalDocumentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PaperlabsTestModule],
        declarations: [LegalDocumentUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(LegalDocumentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LegalDocumentUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LegalDocumentService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new LegalDocument('123');
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new LegalDocument();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
