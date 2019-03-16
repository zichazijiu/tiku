import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TikuSharedModule } from '../../shared';
import {
    ReportItemsService,
    ReportItemsPopupService,
    ReportItemsComponent,
    ReportItemsDetailComponent,
    ReportItemsDialogComponent,
    ReportItemsPopupComponent,
    ReportItemsDeletePopupComponent,
    ReportItemsDeleteDialogComponent,
    reportItemsRoute,
    reportItemsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...reportItemsRoute,
    ...reportItemsPopupRoute,
];

@NgModule({
    imports: [
        TikuSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ReportItemsComponent,
        ReportItemsDetailComponent,
        ReportItemsDialogComponent,
        ReportItemsDeleteDialogComponent,
        ReportItemsPopupComponent,
        ReportItemsDeletePopupComponent,
    ],
    entryComponents: [
        ReportItemsComponent,
        ReportItemsDialogComponent,
        ReportItemsPopupComponent,
        ReportItemsDeleteDialogComponent,
        ReportItemsDeletePopupComponent,
    ],
    providers: [
        ReportItemsService,
        ReportItemsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TikuReportItemsModule {}
