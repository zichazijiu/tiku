import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { RectificationComponent } from './rectification.component';
import { RectificationDetailComponent } from './rectification-detail.component';
import { RectificationPopupComponent } from './rectification-dialog.component';
import { RectificationDeletePopupComponent } from './rectification-delete-dialog.component';

export const rectificationRoute: Routes = [
    {
        path: 'rectification',
        component: RectificationComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.rectification.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'rectification/:id',
        component: RectificationDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.rectification.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const rectificationPopupRoute: Routes = [
    {
        path: 'rectification-new',
        component: RectificationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.rectification.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'rectification/:id/edit',
        component: RectificationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.rectification.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'rectification/:id/delete',
        component: RectificationDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.rectification.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
