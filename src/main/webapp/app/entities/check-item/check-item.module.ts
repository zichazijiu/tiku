import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TikuSharedModule } from '../../shared';
import {
    CheckItemService,
    CheckItemPopupService,
    CheckItemComponent,
    CheckItemDetailComponent,
    CheckItemDialogComponent,
    CheckItemPopupComponent,
    CheckItemDeletePopupComponent,
    CheckItemDeleteDialogComponent,
    checkItemRoute,
    checkItemPopupRoute,
    CheckItemResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...checkItemRoute,
    ...checkItemPopupRoute,
];

@NgModule({
    imports: [
        TikuSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CheckItemComponent,
        CheckItemDetailComponent,
        CheckItemDialogComponent,
        CheckItemDeleteDialogComponent,
        CheckItemPopupComponent,
        CheckItemDeletePopupComponent,
    ],
    entryComponents: [
        CheckItemComponent,
        CheckItemDialogComponent,
        CheckItemPopupComponent,
        CheckItemDeleteDialogComponent,
        CheckItemDeletePopupComponent,
    ],
    providers: [
        CheckItemService,
        CheckItemPopupService,
        CheckItemResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TikuCheckItemModule {}
