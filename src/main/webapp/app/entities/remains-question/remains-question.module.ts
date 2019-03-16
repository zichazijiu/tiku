import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TikuSharedModule } from '../../shared';
import {
    RemainsQuestionService,
    RemainsQuestionPopupService,
    RemainsQuestionComponent,
    RemainsQuestionDetailComponent,
    RemainsQuestionDialogComponent,
    RemainsQuestionPopupComponent,
    RemainsQuestionDeletePopupComponent,
    RemainsQuestionDeleteDialogComponent,
    remainsQuestionRoute,
    remainsQuestionPopupRoute,
} from './';

const ENTITY_STATES = [
    ...remainsQuestionRoute,
    ...remainsQuestionPopupRoute,
];

@NgModule({
    imports: [
        TikuSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        RemainsQuestionComponent,
        RemainsQuestionDetailComponent,
        RemainsQuestionDialogComponent,
        RemainsQuestionDeleteDialogComponent,
        RemainsQuestionPopupComponent,
        RemainsQuestionDeletePopupComponent,
    ],
    entryComponents: [
        RemainsQuestionComponent,
        RemainsQuestionDialogComponent,
        RemainsQuestionPopupComponent,
        RemainsQuestionDeleteDialogComponent,
        RemainsQuestionDeletePopupComponent,
    ],
    providers: [
        RemainsQuestionService,
        RemainsQuestionPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TikuRemainsQuestionModule {}
