import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PaperlabsTestModule } from '../../../test.module';
import { GeneratedLegalDocumentDeleteDialogComponent } from 'app/entities/generated-legal-document/generated-legal-document-delete-dialog.component';
import { GeneratedLegalDocumentService } from 'app/entities/generated-legal-document/generated-legal-document.service';

describe('Component Tests', () => {
  describe('GeneratedLegalDocument Management Delete Component', () => {
    let comp: GeneratedLegalDocumentDeleteDialogComponent;
    let fixture: ComponentFixture<GeneratedLegalDocumentDeleteDialogComponent>;
    let service: GeneratedLegalDocumentService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PaperlabsTestModule],
        declarations: [GeneratedLegalDocumentDeleteDialogComponent]
      })
        .overrideTemplate(GeneratedLegalDocumentDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GeneratedLegalDocumentDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GeneratedLegalDocumentService);
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
