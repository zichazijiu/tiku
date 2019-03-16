import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { RemainsQuestionComponent } from './remains-question.component';
import { RemainsQuestionDetailComponent } from './remains-question-detail.component';
import { RemainsQuestionPopupComponent } from './remains-question-dialog.component';
import { RemainsQuestionDeletePopupComponent } from './remains-question-delete-dialog.component';

export const remainsQuestionRoute: Routes = [
    {
        path: 'remains-question',
        component: RemainsQuestionComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.remainsQuestion.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'remains-question/:id',
        component: RemainsQuestionDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.remainsQuestion.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const remainsQuestionPopupRoute: Routes = [
    {
        path: 'remains-question-new',
        component: RemainsQuestionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.remainsQuestion.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'remains-question/:id/edit',
        component: RemainsQuestionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.remainsQuestion.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'remains-question/:id/delete',
        component: RemainsQuestionDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.remainsQuestion.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
