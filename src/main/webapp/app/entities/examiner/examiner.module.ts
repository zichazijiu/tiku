import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TikuSharedModule } from '../../shared';
import {
    ExaminerService,
    ExaminerPopupService,
    ExaminerComponent,
    ExaminerDetailComponent,
    ExaminerDialogComponent,
    ExaminerPopupComponent,
    ExaminerDeletePopupComponent,
    ExaminerDeleteDialogComponent,
    examinerRoute,
    examinerPopupRoute,
    ExaminerResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...examinerRoute,
    ...examinerPopupRoute,
];

@NgModule({
    imports: [
        TikuSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ExaminerComponent,
        ExaminerDetailComponent,
        ExaminerDialogComponent,
        ExaminerDeleteDialogComponent,
        ExaminerPopupComponent,
        ExaminerDeletePopupComponent,
    ],
    entryComponents: [
        ExaminerComponent,
        ExaminerDialogComponent,
        ExaminerPopupComponent,
        ExaminerDeleteDialogComponent,
        ExaminerDeletePopupComponent,
    ],
    providers: [
        ExaminerService,
        ExaminerPopupService,
        ExaminerResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TikuExaminerModule {}
