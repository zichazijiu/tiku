import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TikuSharedModule } from '../../shared';
import {
    StatisticsService,
    StatisticsPopupService,
    StatisticsComponent,
    StatisticsDetailComponent,
    StatisticsDialogComponent,
    StatisticsPopupComponent,
    StatisticsDeletePopupComponent,
    StatisticsDeleteDialogComponent,
    statisticsRoute,
    statisticsPopupRoute,
    StatisticsResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...statisticsRoute,
    ...statisticsPopupRoute,
];

@NgModule({
    imports: [
        TikuSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        StatisticsComponent,
        StatisticsDetailComponent,
        StatisticsDialogComponent,
        StatisticsDeleteDialogComponent,
        StatisticsPopupComponent,
        StatisticsDeletePopupComponent,
    ],
    entryComponents: [
        StatisticsComponent,
        StatisticsDialogComponent,
        StatisticsPopupComponent,
        StatisticsDeleteDialogComponent,
        StatisticsDeletePopupComponent,
    ],
    providers: [
        StatisticsService,
        StatisticsPopupService,
        StatisticsResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TikuStatisticsModule {}
