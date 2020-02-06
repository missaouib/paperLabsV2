import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDescriptionSection } from 'app/shared/model/description-section.model';

@Component({
  selector: 'jhi-description-section-detail',
  templateUrl: './description-section-detail.component.html'
})
export class DescriptionSectionDetailComponent implements OnInit {
  descriptionSection: IDescriptionSection;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ descriptionSection }) => {
      this.descriptionSection = descriptionSection;
    });
  }

  previousState() {
    window.history.back();
  }
}
