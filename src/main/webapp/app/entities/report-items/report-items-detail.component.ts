import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { ReportItems } from './report-items.model';
import { ReportItemsService } from './report-items.service';

@Component({
    selector: 'jhi-report-items-detail',
    templateUrl: './report-items-detail.component.html'
})
export class ReportItemsDetailComponent implements OnInit, OnDestroy {

    reportItems: ReportItems;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private reportItemsService: ReportItemsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInReportItems();
    }

    load(id) {
        this.reportItemsService.find(id)
            .subscribe((reportItemsResponse: HttpResponse<ReportItems>) => {
                this.reportItems = reportItemsResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInReportItems() {
        this.eventSubscriber = this.eventManager.subscribe(
            'reportItemsListModification',
            (response) => this.load(this.reportItems.id)
        );
    }
}
