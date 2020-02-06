import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PaperlabsTestModule } from '../../../test.module';
import { LawyerUpdateComponent } from 'app/entities/lawyer/lawyer-update.component';
import { LawyerService } from 'app/entities/lawyer/lawyer.service';
import { Lawyer } from 'app/shared/model/lawyer.model';

describe('Component Tests', () => {
  describe('Lawyer Management Update Component', () => {
    let comp: LawyerUpdateComponent;
    let fixture: ComponentFixture<LawyerUpdateComponent>;
    let service: LawyerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PaperlabsTestModule],
        declarations: [LawyerUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(LawyerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LawyerUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LawyerService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Lawyer('123');
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
        const entity = new Lawyer();
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
