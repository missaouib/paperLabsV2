import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEmployer } from 'app/shared/model/employer.model';

@Component({
  selector: 'jhi-employer-detail',
  templateUrl: './employer-detail.component.html'
})
export class EmployerDetailComponent implements OnInit {
  employer: IEmployer;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ employer }) => {
      this.employer = employer;
    });
  }

  previousState() {
    window.history.back();
  }
}
