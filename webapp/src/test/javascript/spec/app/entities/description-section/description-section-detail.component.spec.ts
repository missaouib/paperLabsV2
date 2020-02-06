import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PaperlabsTestModule } from '../../../test.module';
import { DescriptionSectionDetailComponent } from 'app/entities/description-section/description-section-detail.component';
import { DescriptionSection } from 'app/shared/model/description-section.model';

describe('Component Tests', () => {
  describe('DescriptionSection Management Detail Component', () => {
    let comp: DescriptionSectionDetailComponent;
    let fixture: ComponentFixture<DescriptionSectionDetailComponent>;
    const route = ({ data: of({ descriptionSection: new DescriptionSection('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PaperlabsTestModule],
        declarations: [DescriptionSectionDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(DescriptionSectionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DescriptionSectionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.descriptionSection).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
