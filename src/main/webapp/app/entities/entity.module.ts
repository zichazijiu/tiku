import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { TikuProjectModule } from './project/project.module';
import { TikuSubjectModule } from './subject/subject.module';
import { TikuExamineModule } from './examine/examine.module';
import { TikuExaminerModule } from './examiner/examiner.module';
import { TikuStatisticsModule } from './statistics/statistics.module';
import { TikuLogBackupModule } from './log-backup/log-backup.module';
import { TikuDepartmentModule } from './department/department.module';
import { TikuOfficeModule } from './office/office.module';
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
        TikuOfficeModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TikuEntityModule {}
