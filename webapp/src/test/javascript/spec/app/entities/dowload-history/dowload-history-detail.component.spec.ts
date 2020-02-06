import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PaperlabsTestModule } from '../../../test.module';
import { DowloadHistoryDetailComponent } from 'app/entities/dowload-history/dowload-history-detail.component';
import { DowloadHistory } from 'app/shared/model/dowload-history.model';

describe('Component Tests', () => {
  describe('DowloadHistory Management Detail Component', () => {
    let comp: DowloadHistoryDetailComponent;
    let fixture: ComponentFixture<DowloadHistoryDetailComponent>;
    const route = ({ data: of({ dowloadHistory: new DowloadHistory('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PaperlabsTestModule],
        declarations: [DowloadHistoryDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(DowloadHistoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DowloadHistoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.dowloadHistory).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
