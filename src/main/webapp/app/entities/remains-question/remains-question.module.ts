import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TikuSharedModule } from 'app/shared';
import {
  RemainsQuestionComponent,
  RemainsQuestionDetailComponent,
  RemainsQuestionUpdateComponent,
  RemainsQuestionDeletePopupComponent,
  RemainsQuestionDeleteDialogComponent,
  remainsQuestionRoute,
  remainsQuestionPopupRoute
} from './';

const ENTITY_STATES = [...remainsQuestionRoute, ...remainsQuestionPopupRoute];

@NgModule({
  imports: [TikuSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    RemainsQuestionComponent,
    RemainsQuestionDetailComponent,
    RemainsQuestionUpdateComponent,
    RemainsQuestionDeleteDialogComponent,
    RemainsQuestionDeletePopupComponent
  ],
  entryComponents: [
    RemainsQuestionComponent,
    RemainsQuestionUpdateComponent,
    RemainsQuestionDeleteDialogComponent,
    RemainsQuestionDeletePopupComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TikuRemainsQuestionModule {}
