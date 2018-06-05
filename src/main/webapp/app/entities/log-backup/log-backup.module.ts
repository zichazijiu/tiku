import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TikuSharedModule } from '../../shared';
import {
    LogBackupService,
    LogBackupPopupService,
    LogBackupComponent,
    LogBackupDetailComponent,
    LogBackupDialogComponent,
    LogBackupPopupComponent,
    LogBackupDeletePopupComponent,
    LogBackupDeleteDialogComponent,
    logBackupRoute,
    logBackupPopupRoute,
    LogBackupResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...logBackupRoute,
    ...logBackupPopupRoute,
];

@NgModule({
    imports: [
        TikuSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        LogBackupComponent,
        LogBackupDetailComponent,
        LogBackupDialogComponent,
        LogBackupDeleteDialogComponent,
        LogBackupPopupComponent,
        LogBackupDeletePopupComponent,
    ],
    entryComponents: [
        LogBackupComponent,
        LogBackupDialogComponent,
        LogBackupPopupComponent,
        LogBackupDeleteDialogComponent,
        LogBackupDeletePopupComponent,
    ],
    providers: [
        LogBackupService,
        LogBackupPopupService,
        LogBackupResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TikuLogBackupModule {}
