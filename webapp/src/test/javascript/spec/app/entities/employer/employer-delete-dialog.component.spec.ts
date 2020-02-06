import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PaperlabsTestModule } from '../../../test.module';
import { EmployerDeleteDialogComponent } from 'app/entities/employer/employer-delete-dialog.component';
import { EmployerService } from 'app/entities/employer/employer.service';

describe('Component Tests', () => {
  describe('Employer Management Delete Component', () => {
    let comp: EmployerDeleteDialogComponent;
    let fixture: ComponentFixture<EmployerDeleteDialogComponent>;
    let service: EmployerService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PaperlabsTestModule],
        declarations: [EmployerDeleteDialogComponent]
      })
        .overrideTemplate(EmployerDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EmployerDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EmployerService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete('123');
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith('123');
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
