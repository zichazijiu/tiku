import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Report } from './report.model';
import { ReportService } from './report.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-report',
    templateUrl: './report.component.html'
})
export class ReportComponent implements OnInit, OnDestroy {
reports: Report[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private reportService: ReportService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.reportService.query().subscribe(
            (res: HttpResponse<Report[]>) => {
                this.reports = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInReports();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Report) {
        return item.id;
    }
    registerChangeInReports() {
        this.eventSubscriber = this.eventManager.subscribe('reportListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
