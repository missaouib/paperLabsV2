import { Component, OnInit } from '@angular/core';
import { PreviewModalService } from 'app/stepper/preview/preview.service';
import { ActivatedRoute } from '@angular/router';
import { ILegalDocument } from 'app/shared/model/legal-document.model';

@Component({
  selector: 'jhi-preview',
  templateUrl: './preview.component.html',
  styleUrls: ['./preview.component.scss']
})
export class PreviewComponent implements OnInit {
  legalDocument: ILegalDocument;

  constructor(protected previewModalService: PreviewModalService, protected route: ActivatedRoute) {}

  ngOnInit() {
    this.route.data.subscribe(({ legalDocument }) => {
      this.legalDocument = legalDocument;
    });
  }

  displayModal() {
    if (this.legalDocument.shortName === 'CDI') this.previewModalService.open();
  }

  hasPreview(): boolean {
    if (this.legalDocument !== null && this.legalDocument !== undefined) {
      return 'CDI' === this.legalDocument.shortName;
    } else {
      return false;
    }
  }
}
