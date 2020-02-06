import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ILegalDocument } from 'app/shared/model/legal-document.model';
import { JhiAlertService } from 'ng-jhipster';
import { LegalDocumentService } from 'app/entities/legal-document/legal-document.service';

@Component({
  selector: 'jhi-home-page-section-1',
  templateUrl: './home-page-section-1.component.html',
  styleUrls: ['home-page-section-1.scss']
})
export class HomePageSection1Component implements OnInit {
  currentSearch: string;
  legalDocuments: ILegalDocument[] = [];

  @ViewChild('stickyMenu', { static: false }) menuElement: ElementRef;
  elementPosition: any;
  section = 'home-section';

  legalDocumentsSearchResult: ILegalDocument[] = [];

  constructor(private jhiAlertService: JhiAlertService, private legalDocumentService: LegalDocumentService) {}

  scrollToElement($element): void {
    this.section = $element.id.toString();
    $element.scrollIntoView({ behavior: 'smooth', block: 'start', inline: 'nearest' });
  }

  ngOnInit() {
    this.legalDocumentService
      .query()
      .subscribe(
        (res: HttpResponse<ILegalDocument[]>) => (this.legalDocuments = res.body),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  searchLegalDocuments(currentSearch: string) {
    if (currentSearch !== undefined && currentSearch.length > 0) {
      this.legalDocumentService
        .searchLegalDocumentsWithFields(currentSearch)
        .subscribe(
          (res: HttpResponse<ILegalDocument[]>) => (this.legalDocumentsSearchResult = res.body),
          (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
  }
}
