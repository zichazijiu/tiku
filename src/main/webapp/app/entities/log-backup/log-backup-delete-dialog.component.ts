import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { LogBackup } from './log-backup.model';
import { LogBackupPopupService } from './log-backup-popup.service';
import { LogBackupService } from './log-backup.service';

@Component({
    selector: 'jhi-log-backup-delete-dialog',
    templateUrl: './log-backup-delete-dialog.component.html'
})
export class LogBackupDeleteDialogComponent {

    logBackup: LogBackup;

    constructor(
        private logBackupService: LogBackupService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.logBackupService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'logBackupListModification',
                content: 'Deleted an logBackup'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-log-backup-delete-popup',
    template: ''
})
export class LogBackupDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private logBackupPopupService: LogBackupPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.logBackupPopupService
                .open(LogBackupDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
