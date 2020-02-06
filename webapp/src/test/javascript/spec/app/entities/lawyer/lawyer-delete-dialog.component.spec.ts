import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PaperlabsTestModule } from '../../../test.module';
import { LawyerDeleteDialogComponent } from 'app/entities/lawyer/lawyer-delete-dialog.component';
import { LawyerService } from 'app/entities/lawyer/lawyer.service';

describe('Component Tests', () => {
  describe('Lawyer Management Delete Component', () => {
    let comp: LawyerDeleteDialogComponent;
    let fixture: ComponentFixture<LawyerDeleteDialogComponent>;
    let service: LawyerService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PaperlabsTestModule],
        declarations: [LawyerDeleteDialogComponent]
      })
        .overrideTemplate(LawyerDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LawyerDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LawyerService);
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
