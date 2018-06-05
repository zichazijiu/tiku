import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { ExamineComponent } from './examine.component';
import { ExamineDetailComponent } from './examine-detail.component';
import { ExaminePopupComponent } from './examine-dialog.component';
import { ExamineDeletePopupComponent } from './examine-delete-dialog.component';

@Injectable()
export class ExamineResolvePagingParams implements Resolve<any> {

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

export const examineRoute: Routes = [
    {
        path: 'examine',
        component: ExamineComponent,
        resolve: {
            'pagingParams': ExamineResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.examine.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'examine/:id',
        component: ExamineDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.examine.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const examinePopupRoute: Routes = [
    {
        path: 'examine-new',
        component: ExaminePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.examine.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'examine/:id/edit',
        component: ExaminePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.examine.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'examine/:id/delete',
        component: ExamineDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.examine.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
