import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { LogBackup } from './log-backup.model';
import { LogBackupService } from './log-backup.service';

@Component({
    selector: 'jhi-log-backup-detail',
    templateUrl: './log-backup-detail.component.html'
})
export class LogBackupDetailComponent implements OnInit, OnDestroy {

    logBackup: LogBackup;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private logBackupService: LogBackupService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInLogBackups();
    }

    load(id) {
        this.logBackupService.find(id)
            .subscribe((logBackupResponse: HttpResponse<LogBackup>) => {
                this.logBackup = logBackupResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInLogBackups() {
        this.eventSubscriber = this.eventManager.subscribe(
            'logBackupListModification',
            (response) => this.load(this.logBackup.id)
        );
    }
}
