import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { ExaminerComponent } from './examiner.component';
import { ExaminerDetailComponent } from './examiner-detail.component';
import { ExaminerPopupComponent } from './examiner-dialog.component';
import { ExaminerDeletePopupComponent } from './examiner-delete-dialog.component';

@Injectable()
export class ExaminerResolvePagingParams implements Resolve<any> {

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

export const examinerRoute: Routes = [
    {
        path: 'examiner',
        component: ExaminerComponent,
        resolve: {
            'pagingParams': ExaminerResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.examiner.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'examiner/:id',
        component: ExaminerDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.examiner.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const examinerPopupRoute: Routes = [
    {
        path: 'examiner-new',
        component: ExaminerPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.examiner.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'examiner/:id/edit',
        component: ExaminerPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.examiner.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'examiner/:id/delete',
        component: ExaminerDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.examiner.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
