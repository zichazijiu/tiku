import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TikuSharedModule } from 'app/shared';
import {
  RectificationComponent,
  RectificationDetailComponent,
  RectificationUpdateComponent,
  RectificationDeletePopupComponent,
  RectificationDeleteDialogComponent,
  rectificationRoute,
  rectificationPopupRoute
} from './';

const ENTITY_STATES = [...rectificationRoute, ...rectificationPopupRoute];

@NgModule({
  imports: [TikuSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    RectificationComponent,
    RectificationDetailComponent,
    RectificationUpdateComponent,
    RectificationDeleteDialogComponent,
    RectificationDeletePopupComponent
  ],
  entryComponents: [
    RectificationComponent,
    RectificationUpdateComponent,
    RectificationDeleteDialogComponent,
    RectificationDeletePopupComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TikuRectificationModule {}
