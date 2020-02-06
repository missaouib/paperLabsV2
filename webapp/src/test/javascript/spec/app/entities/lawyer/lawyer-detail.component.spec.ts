import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PaperlabsTestModule } from '../../../test.module';
import { LawyerDetailComponent } from 'app/entities/lawyer/lawyer-detail.component';
import { Lawyer } from 'app/shared/model/lawyer.model';

describe('Component Tests', () => {
  describe('Lawyer Management Detail Component', () => {
    let comp: LawyerDetailComponent;
    let fixture: ComponentFixture<LawyerDetailComponent>;
    const route = ({ data: of({ lawyer: new Lawyer('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PaperlabsTestModule],
        declarations: [LawyerDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(LawyerDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LawyerDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.lawyer).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
