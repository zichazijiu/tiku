import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TikuSharedModule } from '../../shared';
import {
    SubjectService,
    SubjectPopupService,
    SubjectComponent,
    SubjectDetailComponent,
    SubjectDialogComponent,
    SubjectPopupComponent,
    SubjectDeletePopupComponent,
    SubjectDeleteDialogComponent,
    subjectRoute,
    subjectPopupRoute,
    SubjectResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...subjectRoute,
    ...subjectPopupRoute,
];

@NgModule({
    imports: [
        TikuSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        SubjectComponent,
        SubjectDetailComponent,
        SubjectDialogComponent,
        SubjectDeleteDialogComponent,
        SubjectPopupComponent,
        SubjectDeletePopupComponent,
    ],
    entryComponents: [
        SubjectComponent,
        SubjectDialogComponent,
        SubjectPopupComponent,
        SubjectDeleteDialogComponent,
        SubjectDeletePopupComponent,
    ],
    providers: [
        SubjectService,
        SubjectPopupService,
        SubjectResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TikuSubjectModule {}
