import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDowloadHistory } from 'app/shared/model/dowload-history.model';

@Component({
  selector: 'jhi-dowload-history-detail',
  templateUrl: './dowload-history-detail.component.html'
})
export class DowloadHistoryDetailComponent implements OnInit {
  dowloadHistory: IDowloadHistory;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ dowloadHistory }) => {
      this.dowloadHistory = dowloadHistory;
    });
  }

  previousState() {
    window.history.back();
  }
}
