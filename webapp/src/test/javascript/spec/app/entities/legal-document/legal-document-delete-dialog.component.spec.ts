import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PaperlabsTestModule } from '../../../test.module';
import { LegalDocumentDeleteDialogComponent } from 'app/entities/legal-document/legal-document-delete-dialog.component';
import { LegalDocumentService } from 'app/entities/legal-document/legal-document.service';

describe('Component Tests', () => {
  describe('LegalDocument Management Delete Component', () => {
    let comp: LegalDocumentDeleteDialogComponent;
    let fixture: ComponentFixture<LegalDocumentDeleteDialogComponent>;
    let service: LegalDocumentService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PaperlabsTestModule],
        declarations: [LegalDocumentDeleteDialogComponent]
      })
        .overrideTemplate(LegalDocumentDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LegalDocumentDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LegalDocumentService);
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
