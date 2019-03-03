import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { CheckItemAnswerComponent } from './check-item-answer.component';
import { CheckItemAnswerDetailComponent } from './check-item-answer-detail.component';
import { CheckItemAnswerPopupComponent } from './check-item-answer-dialog.component';
import { CheckItemAnswerDeletePopupComponent } from './check-item-answer-delete-dialog.component';

export const checkItemAnswerRoute: Routes = [
    {
        path: 'check-item-answer',
        component: CheckItemAnswerComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.checkItemAnswer.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'check-item-answer/:id',
        component: CheckItemAnswerDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.checkItemAnswer.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const checkItemAnswerPopupRoute: Routes = [
    {
        path: 'check-item-answer-new',
        component: CheckItemAnswerPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.checkItemAnswer.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'check-item-answer/:id/edit',
        component: CheckItemAnswerPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.checkItemAnswer.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'check-item-answer/:id/delete',
        component: CheckItemAnswerDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.checkItemAnswer.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
