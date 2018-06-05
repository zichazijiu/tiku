import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { LogBackup } from './log-backup.model';
import { LogBackupPopupService } from './log-backup-popup.service';
import { LogBackupService } from './log-backup.service';

@Component({
    selector: 'jhi-log-backup-dialog',
    templateUrl: './log-backup-dialog.component.html'
})
export class LogBackupDialogComponent implements OnInit {

    logBackup: LogBackup;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private logBackupService: LogBackupService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.logBackup.id !== undefined) {
            this.subscribeToSaveResponse(
                this.logBackupService.update(this.logBackup));
        } else {
            this.subscribeToSaveResponse(
                this.logBackupService.create(this.logBackup));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<LogBackup>>) {
        result.subscribe((res: HttpResponse<LogBackup>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: LogBackup) {
        this.eventManager.broadcast({ name: 'logBackupListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-log-backup-popup',
    template: ''
})
export class LogBackupPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private logBackupPopupService: LogBackupPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.logBackupPopupService
                    .open(LogBackupDialogComponent as Component, params['id']);
            } else {
                this.logBackupPopupService
                    .open(LogBackupDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
