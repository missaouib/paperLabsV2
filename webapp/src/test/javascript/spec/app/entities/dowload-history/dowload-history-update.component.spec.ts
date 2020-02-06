import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PaperlabsTestModule } from '../../../test.module';
import { DowloadHistoryUpdateComponent } from 'app/entities/dowload-history/dowload-history-update.component';
import { DowloadHistoryService } from 'app/entities/dowload-history/dowload-history.service';
import { DowloadHistory } from 'app/shared/model/dowload-history.model';

describe('Component Tests', () => {
  describe('DowloadHistory Management Update Component', () => {
    let comp: DowloadHistoryUpdateComponent;
    let fixture: ComponentFixture<DowloadHistoryUpdateComponent>;
    let service: DowloadHistoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PaperlabsTestModule],
        declarations: [DowloadHistoryUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(DowloadHistoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DowloadHistoryUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DowloadHistoryService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new DowloadHistory('123');
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
        const entity = new DowloadHistory();
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
