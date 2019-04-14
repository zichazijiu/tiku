import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { ReleaseComponent } from './release.component';
import { ReleaseDetailComponent } from './release-detail.component';
import { ReleasePopupComponent } from './release-dialog.component';
import { ReleaseDeletePopupComponent } from './release-delete-dialog.component';

export const releaseRoute: Routes = [
    {
        path: 'release',
        component: ReleaseComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.release.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'release/:id',
        component: ReleaseDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.release.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const releasePopupRoute: Routes = [
    {
        path: 'release-new',
        component: ReleasePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.release.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'release/:id/edit',
        component: ReleasePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.release.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'release/:id/delete',
        component: ReleaseDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.release.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
