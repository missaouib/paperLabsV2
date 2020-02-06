import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PaperlabsTestModule } from '../../../test.module';
import { EmployerComponent } from 'app/entities/employer/employer.component';
import { EmployerService } from 'app/entities/employer/employer.service';
import { Employer } from 'app/shared/model/employer.model';

describe('Component Tests', () => {
  describe('Employer Management Component', () => {
    let comp: EmployerComponent;
    let fixture: ComponentFixture<EmployerComponent>;
    let service: EmployerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PaperlabsTestModule],
        declarations: [EmployerComponent],
        providers: []
      })
        .overrideTemplate(EmployerComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EmployerComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EmployerService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Employer('123')],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.employers[0]).toEqual(jasmine.objectContaining({ id: '123' }));
    });
  });
});
