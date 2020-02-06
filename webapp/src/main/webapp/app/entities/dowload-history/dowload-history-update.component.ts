import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IDowloadHistory, DowloadHistory } from 'app/shared/model/dowload-history.model';
import { DowloadHistoryService } from './dowload-history.service';

@Component({
  selector: 'jhi-dowload-history-update',
  templateUrl: './dowload-history-update.component.html'
})
export class DowloadHistoryUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]]
  });

  constructor(protected dowloadHistoryService: DowloadHistoryService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ dowloadHistory }) => {
      this.updateForm(dowloadHistory);
    });
  }

  updateForm(dowloadHistory: IDowloadHistory) {
    this.editForm.patchValue({
      id: dowloadHistory.id,
      date: dowloadHistory.date != null ? dowloadHistory.date.format(DATE_TIME_FORMAT) : null
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const dowloadHistory = this.createFromForm();
    if (dowloadHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.dowloadHistoryService.update(dowloadHistory));
    } else {
      this.subscribeToSaveResponse(this.dowloadHistoryService.create(dowloadHistory));
    }
  }

  private createFromForm(): IDowloadHistory {
    return {
      ...new DowloadHistory(),
      id: this.editForm.get(['id']).value,
      date: this.editForm.get(['date']).value != null ? moment(this.editForm.get(['date']).value, DATE_TIME_FORMAT) : undefined
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDowloadHistory>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
