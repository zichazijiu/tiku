import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TikuSharedModule } from '../../shared';
import {
    CheckItemAnswerService,
    CheckItemAnswerPopupService,
    CheckItemAnswerComponent,
    CheckItemAnswerDetailComponent,
    CheckItemAnswerDialogComponent,
    CheckItemAnswerPopupComponent,
    CheckItemAnswerDeletePopupComponent,
    CheckItemAnswerDeleteDialogComponent,
    checkItemAnswerRoute,
    checkItemAnswerPopupRoute,
} from './';

const ENTITY_STATES = [
    ...checkItemAnswerRoute,
    ...checkItemAnswerPopupRoute,
];

@NgModule({
    imports: [
        TikuSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CheckItemAnswerComponent,
        CheckItemAnswerDetailComponent,
        CheckItemAnswerDialogComponent,
        CheckItemAnswerDeleteDialogComponent,
        CheckItemAnswerPopupComponent,
        CheckItemAnswerDeletePopupComponent,
    ],
    entryComponents: [
        CheckItemAnswerComponent,
        CheckItemAnswerDialogComponent,
        CheckItemAnswerPopupComponent,
        CheckItemAnswerDeleteDialogComponent,
        CheckItemAnswerDeletePopupComponent,
    ],
    providers: [
        CheckItemAnswerService,
        CheckItemAnswerPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TikuCheckItemAnswerModule {}
