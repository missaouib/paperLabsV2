import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILegalDocument } from 'app/shared/model/legal-document.model';
import { LegalDocumentService } from 'app/entities/legal-document/legal-document.service';

@Component({
  selector: 'jhi-legal-document-detail',
  templateUrl: './legal-document-detail.component.html'
})
export class LegalDocumentDetailComponent implements OnInit {
  legalDocument: ILegalDocument;
  numberOfDownloads = 0;
  numberOfCanceledDownload = 0;
  sumDocumentRevenue = 0;

  constructor(protected activatedRoute: ActivatedRoute, protected legalDocumentService: LegalDocumentService) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ legalDocument }) => {
      this.legalDocument = legalDocument;
    });
    this.getTotalDownloads();
    this.getNumberOfCanceledDownload();
    this.getSumDocumentRevenue();
  }

  previousState() {
    window.history.back();
  }

  getTotalDownloads() {
    this.legalDocumentService.getDocumentStatisticsInfo(this.legalDocument.id).subscribe(info => {
      this.numberOfDownloads = info.body.numberOfDownload;
    });
  }

  getNumberOfCanceledDownload() {
    this.legalDocumentService.getDocumentStatisticsInfo(this.legalDocument.id).subscribe(info => {
      this.numberOfCanceledDownload = info.body.numberOfCanceledDownload;
    });
  }

  getSumDocumentRevenue() {
    this.legalDocumentService.getDocumentStatisticsInfo(this.legalDocument.id).subscribe(info => {
      this.sumDocumentRevenue = info.body.sumDocumentRevenue;
    });
  }
}
