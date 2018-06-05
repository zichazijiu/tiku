import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { SubjectComponent } from './subject.component';
import { SubjectDetailComponent } from './subject-detail.component';
import { SubjectPopupComponent } from './subject-dialog.component';
import { SubjectDeletePopupComponent } from './subject-delete-dialog.component';

@Injectable()
export class SubjectResolvePagingParams implements Resolve<any> {

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

export const subjectRoute: Routes = [
    {
        path: 'subject',
        component: SubjectComponent,
        resolve: {
            'pagingParams': SubjectResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.subject.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'subject/:id',
        component: SubjectDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.subject.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const subjectPopupRoute: Routes = [
    {
        path: 'subject-new',
        component: SubjectPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.subject.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'subject/:id/edit',
        component: SubjectPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.subject.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'subject/:id/delete',
        component: SubjectDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.subject.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
