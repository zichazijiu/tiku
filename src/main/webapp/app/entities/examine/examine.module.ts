import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TikuSharedModule } from '../../shared';
import {
    ExamineService,
    ExaminePopupService,
    ExamineComponent,
    ExamineDetailComponent,
    ExamineDialogComponent,
    ExaminePopupComponent,
    ExamineDeletePopupComponent,
    ExamineDeleteDialogComponent,
    examineRoute,
    examinePopupRoute,
    ExamineResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...examineRoute,
    ...examinePopupRoute,
];

@NgModule({
    imports: [
        TikuSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ExamineComponent,
        ExamineDetailComponent,
        ExamineDialogComponent,
        ExamineDeleteDialogComponent,
        ExaminePopupComponent,
        ExamineDeletePopupComponent,
    ],
    entryComponents: [
        ExamineComponent,
        ExamineDialogComponent,
        ExaminePopupComponent,
        ExamineDeleteDialogComponent,
        ExamineDeletePopupComponent,
    ],
    providers: [
        ExamineService,
        ExaminePopupService,
        ExamineResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TikuExamineModule {}
