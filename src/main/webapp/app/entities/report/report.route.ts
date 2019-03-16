import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { ReportComponent } from './report.component';
import { ReportDetailComponent } from './report-detail.component';
import { ReportPopupComponent } from './report-dialog.component';
import { ReportDeletePopupComponent } from './report-delete-dialog.component';

export const reportRoute: Routes = [
    {
        path: 'report',
        component: ReportComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.report.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'report/:id',
        component: ReportDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.report.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const reportPopupRoute: Routes = [
    {
        path: 'report-new',
        component: ReportPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.report.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'report/:id/edit',
        component: ReportPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.report.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'report/:id/delete',
        component: ReportDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.report.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
