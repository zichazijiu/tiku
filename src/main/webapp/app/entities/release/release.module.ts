import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TikuSharedModule } from '../../shared';
import {
    ReleaseService,
    ReleasePopupService,
    ReleaseComponent,
    ReleaseDetailComponent,
    ReleaseDialogComponent,
    ReleasePopupComponent,
    ReleaseDeletePopupComponent,
    ReleaseDeleteDialogComponent,
    releaseRoute,
    releasePopupRoute,
} from './';

const ENTITY_STATES = [
    ...releaseRoute,
    ...releasePopupRoute,
];

@NgModule({
    imports: [
        TikuSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ReleaseComponent,
        ReleaseDetailComponent,
        ReleaseDialogComponent,
        ReleaseDeleteDialogComponent,
        ReleasePopupComponent,
        ReleaseDeletePopupComponent,
    ],
    entryComponents: [
        ReleaseComponent,
        ReleaseDialogComponent,
        ReleasePopupComponent,
        ReleaseDeleteDialogComponent,
        ReleaseDeletePopupComponent,
    ],
    providers: [
        ReleaseService,
        ReleasePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TikuReleaseModule {}
