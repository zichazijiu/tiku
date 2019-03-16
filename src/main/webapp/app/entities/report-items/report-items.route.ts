import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { ReportItemsComponent } from './report-items.component';
import { ReportItemsDetailComponent } from './report-items-detail.component';
import { ReportItemsPopupComponent } from './report-items-dialog.component';
import { ReportItemsDeletePopupComponent } from './report-items-delete-dialog.component';

export const reportItemsRoute: Routes = [
    {
        path: 'report-items',
        component: ReportItemsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.reportItems.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'report-items/:id',
        component: ReportItemsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.reportItems.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const reportItemsPopupRoute: Routes = [
    {
        path: 'report-items-new',
        component: ReportItemsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.reportItems.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'report-items/:id/edit',
        component: ReportItemsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.reportItems.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'report-items/:id/delete',
        component: ReportItemsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.reportItems.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
