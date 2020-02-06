import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PaperlabsTestModule } from '../../../test.module';
import { DescriptionSectionUpdateComponent } from 'app/entities/description-section/description-section-update.component';
import { DescriptionSectionService } from 'app/entities/description-section/description-section.service';
import { DescriptionSection } from 'app/shared/model/description-section.model';

describe('Component Tests', () => {
  describe('DescriptionSection Management Update Component', () => {
    let comp: DescriptionSectionUpdateComponent;
    let fixture: ComponentFixture<DescriptionSectionUpdateComponent>;
    let service: DescriptionSectionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PaperlabsTestModule],
        declarations: [DescriptionSectionUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(DescriptionSectionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DescriptionSectionUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DescriptionSectionService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new DescriptionSection('123');
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
        const entity = new DescriptionSection();
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
