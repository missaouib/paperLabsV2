import { Route } from '@angular/router';
import { QuestionForLawyerComponent } from 'app/user/question-for-lawyer/question-for-lawyer.component';

export const questionForLawyerRoute: Route = {
  path: 'question-for-lawyer',
  component: QuestionForLawyerComponent,
  data: {
    pageTitle: 'paperlabsApp.questionForLawyer.title'
  }
};
