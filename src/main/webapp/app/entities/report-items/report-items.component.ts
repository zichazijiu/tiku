import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ReportItems } from './report-items.model';
import { ReportItemsService } from './report-items.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-report-items',
    templateUrl: './report-items.component.html'
})
export class ReportItemsComponent implements OnInit, OnDestroy {
reportItems: ReportItems[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private reportItemsService: ReportItemsService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.reportItemsService.query().subscribe(
            (res: HttpResponse<ReportItems[]>) => {
                this.reportItems = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInReportItems();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ReportItems) {
        return item.id;
    }
    registerChangeInReportItems() {
        this.eventSubscriber = this.eventManager.subscribe('reportItemsListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
