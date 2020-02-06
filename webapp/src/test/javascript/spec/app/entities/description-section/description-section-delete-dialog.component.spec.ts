import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PaperlabsTestModule } from '../../../test.module';
import { DescriptionSectionDeleteDialogComponent } from 'app/entities/description-section/description-section-delete-dialog.component';
import { DescriptionSectionService } from 'app/entities/description-section/description-section.service';

describe('Component Tests', () => {
  describe('DescriptionSection Management Delete Component', () => {
    let comp: DescriptionSectionDeleteDialogComponent;
    let fixture: ComponentFixture<DescriptionSectionDeleteDialogComponent>;
    let service: DescriptionSectionService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PaperlabsTestModule],
        declarations: [DescriptionSectionDeleteDialogComponent]
      })
        .overrideTemplate(DescriptionSectionDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DescriptionSectionDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DescriptionSectionService);
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
