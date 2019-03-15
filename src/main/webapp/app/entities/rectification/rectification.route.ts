import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Rectification } from 'app/shared/model/rectification.model';
import { RectificationService } from './rectification.service';
import { RectificationComponent } from './rectification.component';
import { RectificationDetailComponent } from './rectification-detail.component';
import { RectificationUpdateComponent } from './rectification-update.component';
import { RectificationDeletePopupComponent } from './rectification-delete-dialog.component';
import { IRectification } from 'app/shared/model/rectification.model';

@Injectable({ providedIn: 'root' })
export class RectificationResolve implements Resolve<IRectification> {
  constructor(private service: RectificationService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(map((rectification: HttpResponse<Rectification>) => rectification.body));
    }
    return of(new Rectification());
  }
}

export const rectificationRoute: Routes = [
  {
    path: 'rectification',
    component: RectificationComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tikuApp.rectification.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'rectification/:id/view',
    component: RectificationDetailComponent,
    resolve: {
      rectification: RectificationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tikuApp.rectification.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'rectification/new',
    component: RectificationUpdateComponent,
    resolve: {
      rectification: RectificationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tikuApp.rectification.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'rectification/:id/edit',
    component: RectificationUpdateComponent,
    resolve: {
      rectification: RectificationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tikuApp.rectification.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const rectificationPopupRoute: Routes = [
  {
    path: 'rectification/:id/delete',
    component: RectificationDeletePopupComponent,
    resolve: {
      rectification: RectificationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tikuApp.rectification.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
