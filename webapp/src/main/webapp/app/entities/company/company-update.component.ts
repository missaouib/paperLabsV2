import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { ICompany, Company } from 'app/shared/model/company.model';
import { CompanyService } from './company.service';

@Component({
  selector: 'jhi-company-update',
  templateUrl: './company-update.component.html'
})
export class CompanyUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    companyName: [],
    companyCapital: [],
    companyUniqueIdentification: [],
    companyRepresentativeFullName: [],
    companyFullAddress: [],
    companyType: []
  });

  constructor(protected companyService: CompanyService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ company }) => {
      this.updateForm(company);
    });
  }

  updateForm(company: ICompany) {
    this.editForm.patchValue({
      id: company.id,
      companyName: company.companyName,
      companyCapital: company.companyCapital,
      companyUniqueIdentification: company.companyUniqueIdentification,
      companyRepresentativeFullName: company.companyRepresentativeFullName,
      companyFullAddress: company.companyFullAddress,
      companyType: company.companyType
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const company = this.createFromForm();
    if (company.id !== undefined) {
      this.subscribeToSaveResponse(this.companyService.update(company));
    } else {
      this.subscribeToSaveResponse(this.companyService.create(company));
    }
  }

  private createFromForm(): ICompany {
    return {
      ...new Company(),
      id: this.editForm.get(['id']).value,
      companyName: this.editForm.get(['companyName']).value,
      companyCapital: this.editForm.get(['companyCapital']).value,
      companyUniqueIdentification: this.editForm.get(['companyUniqueIdentification']).value,
      companyRepresentativeFullName: this.editForm.get(['companyRepresentativeFullName']).value,
      companyFullAddress: this.editForm.get(['companyFullAddress']).value,
      companyType: this.editForm.get(['companyType']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompany>>) {
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
