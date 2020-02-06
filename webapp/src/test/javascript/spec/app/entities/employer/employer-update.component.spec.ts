import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PaperlabsTestModule } from '../../../test.module';
import { EmployerUpdateComponent } from 'app/entities/employer/employer-update.component';
import { EmployerService } from 'app/entities/employer/employer.service';
import { Employer } from 'app/shared/model/employer.model';

describe('Component Tests', () => {
  describe('Employer Management Update Component', () => {
    let comp: EmployerUpdateComponent;
    let fixture: ComponentFixture<EmployerUpdateComponent>;
    let service: EmployerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PaperlabsTestModule],
        declarations: [EmployerUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(EmployerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EmployerUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EmployerService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Employer('123');
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
        const entity = new Employer();
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
