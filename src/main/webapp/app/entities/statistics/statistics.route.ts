import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { StatisticsComponent } from './statistics.component';
import { StatisticsDetailComponent } from './statistics-detail.component';
import { StatisticsPopupComponent } from './statistics-dialog.component';
import { StatisticsDeletePopupComponent } from './statistics-delete-dialog.component';

@Injectable()
export class StatisticsResolvePagingParams implements Resolve<any> {

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

export const statisticsRoute: Routes = [
    {
        path: 'statistics',
        component: StatisticsComponent,
        resolve: {
            'pagingParams': StatisticsResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.statistics.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'statistics/:id',
        component: StatisticsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.statistics.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const statisticsPopupRoute: Routes = [
    {
        path: 'statistics-new',
        component: StatisticsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.statistics.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'statistics/:id/edit',
        component: StatisticsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.statistics.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'statistics/:id/delete',
        component: StatisticsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.statistics.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
