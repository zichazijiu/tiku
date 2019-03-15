import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { TikuProjectModule } from './project/project.module';
import { TikuSubjectModule } from './subject/subject.module';
import { TikuExamineModule } from './examine/examine.module';
import { TikuExaminerModule } from './examiner/examiner.module';
import { TikuStatisticsModule } from './statistics/statistics.module';
import { TikuLogBackupModule } from './log-backup/log-backup.module';
import { TikuDepartmentModule } from './department/department.module';
import { TikuMenuModule } from './menu/menu.module';
import { TikuCheckItemModule } from './check-item/check-item.module';
import { TikuCheckItemAnswerModule } from './check-item-answer/check-item-answer.module';
import { TikuRemainsQuestionModule } from './remains-question/remains-question.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
  imports: [
    TikuProjectModule,
    TikuSubjectModule,
    TikuExamineModule,
    TikuExaminerModule,
    TikuStatisticsModule,
    TikuLogBackupModule,
    TikuDepartmentModule,
    TikuMenuModule,
    TikuCheckItemModule,
    TikuCheckItemAnswerModule,
    TikuRemainsQuestionModule
    /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TikuEntityModule {}
