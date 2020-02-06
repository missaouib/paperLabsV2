import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGeneratedLegalDocument } from 'app/shared/model/generated-legal-document.model';

@Component({
  selector: 'jhi-generated-legal-document-detail',
  templateUrl: './generated-legal-document-detail.component.html'
})
export class GeneratedLegalDocumentDetailComponent implements OnInit {
  generatedLegalDocument: IGeneratedLegalDocument;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ generatedLegalDocument }) => {
      this.generatedLegalDocument = generatedLegalDocument;
    });
  }

  previousState() {
    window.history.back();
  }
}
