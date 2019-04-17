import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { ReleaseHistoryComponent } from './release-history.component';
import { ReleaseHistoryDetailComponent } from './release-history-detail.component';
import { ReleaseHistoryPopupComponent } from './release-history-dialog.component';
import { ReleaseHistoryDeletePopupComponent } from './release-history-delete-dialog.component';

export const releaseHistoryRoute: Routes = [
    {
        path: 'release-history',
        component: ReleaseHistoryComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.releaseHistory.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'release-history/:id',
        component: ReleaseHistoryDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.releaseHistory.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const releaseHistoryPopupRoute: Routes = [
    {
        path: 'release-history-new',
        component: ReleaseHistoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.releaseHistory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'release-history/:id/edit',
        component: ReleaseHistoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.releaseHistory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'release-history/:id/delete',
        component: ReleaseHistoryDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.releaseHistory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
