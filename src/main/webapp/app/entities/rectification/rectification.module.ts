import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TikuSharedModule } from '../../shared';
import {
    RectificationService,
    RectificationPopupService,
    RectificationComponent,
    RectificationDetailComponent,
    RectificationDialogComponent,
    RectificationPopupComponent,
    RectificationDeletePopupComponent,
    RectificationDeleteDialogComponent,
    rectificationRoute,
    rectificationPopupRoute,
} from './';

const ENTITY_STATES = [
    ...rectificationRoute,
    ...rectificationPopupRoute,
];

@NgModule({
    imports: [
        TikuSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        RectificationComponent,
        RectificationDetailComponent,
        RectificationDialogComponent,
        RectificationDeleteDialogComponent,
        RectificationPopupComponent,
        RectificationDeletePopupComponent,
    ],
    entryComponents: [
        RectificationComponent,
        RectificationDialogComponent,
        RectificationPopupComponent,
        RectificationDeleteDialogComponent,
        RectificationDeletePopupComponent,
    ],
    providers: [
        RectificationService,
        RectificationPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TikuRectificationModule {}
