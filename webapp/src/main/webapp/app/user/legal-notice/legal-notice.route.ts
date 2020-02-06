import { Route } from '@angular/router';
import { LegalNoticeComponent } from 'app/user/legal-notice/legal-notice.component';

export const legalNoticeRoute: Route = {
  path: 'legal-notice',
  component: LegalNoticeComponent,
  data: {
    pageTitle: 'paperlabsApp.legalNotice.title'
  }
};
