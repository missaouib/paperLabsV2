import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PaperlabsTestModule } from '../../../test.module';
import { GeneratedLegalDocumentUpdateComponent } from 'app/entities/generated-legal-document/generated-legal-document-update.component';
import { GeneratedLegalDocumentService } from 'app/entities/generated-legal-document/generated-legal-document.service';
import { GeneratedLegalDocument } from 'app/shared/model/generated-legal-document.model';

describe('Component Tests', () => {
  describe('GeneratedLegalDocument Management Update Component', () => {
    let comp: GeneratedLegalDocumentUpdateComponent;
    let fixture: ComponentFixture<GeneratedLegalDocumentUpdateComponent>;
    let service: GeneratedLegalDocumentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PaperlabsTestModule],
        declarations: [GeneratedLegalDocumentUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(GeneratedLegalDocumentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GeneratedLegalDocumentUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GeneratedLegalDocumentService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new GeneratedLegalDocument('123');
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
        const entity = new GeneratedLegalDocument();
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
