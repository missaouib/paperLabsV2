import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PaperlabsTestModule } from '../../../test.module';
import { DowloadHistoryDeleteDialogComponent } from 'app/entities/dowload-history/dowload-history-delete-dialog.component';
import { DowloadHistoryService } from 'app/entities/dowload-history/dowload-history.service';

describe('Component Tests', () => {
  describe('DowloadHistory Management Delete Component', () => {
    let comp: DowloadHistoryDeleteDialogComponent;
    let fixture: ComponentFixture<DowloadHistoryDeleteDialogComponent>;
    let service: DowloadHistoryService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PaperlabsTestModule],
        declarations: [DowloadHistoryDeleteDialogComponent]
      })
        .overrideTemplate(DowloadHistoryDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DowloadHistoryDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DowloadHistoryService);
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
