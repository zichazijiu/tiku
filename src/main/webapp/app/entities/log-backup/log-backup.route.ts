import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { LogBackupComponent } from './log-backup.component';
import { LogBackupDetailComponent } from './log-backup-detail.component';
import { LogBackupPopupComponent } from './log-backup-dialog.component';
import { LogBackupDeletePopupComponent } from './log-backup-delete-dialog.component';

@Injectable()
export class LogBackupResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const logBackupRoute: Routes = [
    {
        path: 'log-backup',
        component: LogBackupComponent,
        resolve: {
            'pagingParams': LogBackupResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.logBackup.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'log-backup/:id',
        component: LogBackupDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.logBackup.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const logBackupPopupRoute: Routes = [
    {
        path: 'log-backup-new',
        component: LogBackupPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.logBackup.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'log-backup/:id/edit',
        component: LogBackupPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.logBackup.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'log-backup/:id/delete',
        component: LogBackupDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.logBackup.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
