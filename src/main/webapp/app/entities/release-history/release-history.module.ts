import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TikuSharedModule } from '../../shared';
import {
    ReleaseHistoryService,
    ReleaseHistoryPopupService,
    ReleaseHistoryComponent,
    ReleaseHistoryDetailComponent,
    ReleaseHistoryDialogComponent,
    ReleaseHistoryPopupComponent,
    ReleaseHistoryDeletePopupComponent,
    ReleaseHistoryDeleteDialogComponent,
    releaseHistoryRoute,
    releaseHistoryPopupRoute,
} from './';

const ENTITY_STATES = [
    ...releaseHistoryRoute,
    ...releaseHistoryPopupRoute,
];

@NgModule({
    imports: [
        TikuSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ReleaseHistoryComponent,
        ReleaseHistoryDetailComponent,
        ReleaseHistoryDialogComponent,
        ReleaseHistoryDeleteDialogComponent,
        ReleaseHistoryPopupComponent,
        ReleaseHistoryDeletePopupComponent,
    ],
    entryComponents: [
        ReleaseHistoryComponent,
        ReleaseHistoryDialogComponent,
        ReleaseHistoryPopupComponent,
        ReleaseHistoryDeleteDialogComponent,
        ReleaseHistoryDeletePopupComponent,
    ],
    providers: [
        ReleaseHistoryService,
        ReleaseHistoryPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TikuReleaseHistoryModule {}
