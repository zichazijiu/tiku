import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TikuSharedModule } from '../../shared';
import { TikuAdminModule } from '../../admin/admin.module';
import {
    ReportService,
    ReportPopupService,
    ReportComponent,
    ReportDetailComponent,
    ReportDialogComponent,
    ReportPopupComponent,
    ReportDeletePopupComponent,
    ReportDeleteDialogComponent,
    reportRoute,
    reportPopupRoute,
} from './';

const ENTITY_STATES = [
    ...reportRoute,
    ...reportPopupRoute,
];

@NgModule({
    imports: [
        TikuSharedModule,
        TikuAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ReportComponent,
        ReportDetailComponent,
        ReportDialogComponent,
        ReportDeleteDialogComponent,
        ReportPopupComponent,
        ReportDeletePopupComponent,
    ],
    entryComponents: [
        ReportComponent,
        ReportDialogComponent,
        ReportPopupComponent,
        ReportDeleteDialogComponent,
        ReportDeletePopupComponent,
    ],
    providers: [
        ReportService,
        ReportPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TikuReportModule {}
