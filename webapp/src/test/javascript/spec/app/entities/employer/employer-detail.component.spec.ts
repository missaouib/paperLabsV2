import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PaperlabsTestModule } from '../../../test.module';
import { EmployerDetailComponent } from 'app/entities/employer/employer-detail.component';
import { Employer } from 'app/shared/model/employer.model';

describe('Component Tests', () => {
  describe('Employer Management Detail Component', () => {
    let comp: EmployerDetailComponent;
    let fixture: ComponentFixture<EmployerDetailComponent>;
    const route = ({ data: of({ employer: new Employer('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PaperlabsTestModule],
        declarations: [EmployerDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(EmployerDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EmployerDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.employer).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
