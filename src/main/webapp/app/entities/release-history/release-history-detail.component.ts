import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { ReleaseHistory } from './release-history.model';
import { ReleaseHistoryService } from './release-history.service';

@Component({
    selector: 'jhi-release-history-detail',
    templateUrl: './release-history-detail.component.html'
})
export class ReleaseHistoryDetailComponent implements OnInit, OnDestroy {

    releaseHistory: ReleaseHistory;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private releaseHistoryService: ReleaseHistoryService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInReleaseHistories();
    }

    load(id) {
        this.releaseHistoryService.find(id)
            .subscribe((releaseHistoryResponse: HttpResponse<ReleaseHistory>) => {
                this.releaseHistory = releaseHistoryResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInReleaseHistories() {
        this.eventSubscriber = this.eventManager.subscribe(
            'releaseHistoryListModification',
            (response) => this.load(this.releaseHistory.id)
        );
    }
}
