import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { RemainsQuestion } from 'app/shared/model/remains-question.model';
import { RemainsQuestionService } from './remains-question.service';
import { RemainsQuestionComponent } from './remains-question.component';
import { RemainsQuestionDetailComponent } from './remains-question-detail.component';
import { RemainsQuestionUpdateComponent } from './remains-question-update.component';
import { RemainsQuestionDeletePopupComponent } from './remains-question-delete-dialog.component';
import { IRemainsQuestion } from 'app/shared/model/remains-question.model';

@Injectable({ providedIn: 'root' })
export class RemainsQuestionResolve implements Resolve<IRemainsQuestion> {
  constructor(private service: RemainsQuestionService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(map((remainsQuestion: HttpResponse<RemainsQuestion>) => remainsQuestion.body));
    }
    return of(new RemainsQuestion());
  }
}

export const remainsQuestionRoute: Routes = [
  {
    path: 'remains-question',
    component: RemainsQuestionComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tikuApp.remainsQuestion.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'remains-question/:id/view',
    component: RemainsQuestionDetailComponent,
    resolve: {
      remainsQuestion: RemainsQuestionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tikuApp.remainsQuestion.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'remains-question/new',
    component: RemainsQuestionUpdateComponent,
    resolve: {
      remainsQuestion: RemainsQuestionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tikuApp.remainsQuestion.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'remains-question/:id/edit',
    component: RemainsQuestionUpdateComponent,
    resolve: {
      remainsQuestion: RemainsQuestionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tikuApp.remainsQuestion.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const remainsQuestionPopupRoute: Routes = [
  {
    path: 'remains-question/:id/delete',
    component: RemainsQuestionDeletePopupComponent,
    resolve: {
      remainsQuestion: RemainsQuestionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tikuApp.remainsQuestion.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
