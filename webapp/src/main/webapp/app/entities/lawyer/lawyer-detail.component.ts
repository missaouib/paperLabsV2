import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILawyer } from 'app/shared/model/lawyer.model';

@Component({
  selector: 'jhi-lawyer-detail',
  templateUrl: './lawyer-detail.component.html'
})
export class LawyerDetailComponent implements OnInit {
  lawyer: ILawyer;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ lawyer }) => {
      this.lawyer = lawyer;
    });
  }

  previousState() {
    window.history.back();
  }
}
