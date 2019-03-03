import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { CheckItemComponent } from './check-item.component';
import { CheckItemDetailComponent } from './check-item-detail.component';
import { CheckItemPopupComponent } from './check-item-dialog.component';
import { CheckItemDeletePopupComponent } from './check-item-delete-dialog.component';

@Injectable()
export class CheckItemResolvePagingParams implements Resolve<any> {

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

export const checkItemRoute: Routes = [
    {
        path: 'check-item',
        component: CheckItemComponent,
        resolve: {
            'pagingParams': CheckItemResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.checkItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'check-item/:id',
        component: CheckItemDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.checkItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const checkItemPopupRoute: Routes = [
    {
        path: 'check-item-new',
        component: CheckItemPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.checkItem.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'check-item/:id/edit',
        component: CheckItemPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.checkItem.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'check-item/:id/delete',
        component: CheckItemDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tikuApp.checkItem.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
