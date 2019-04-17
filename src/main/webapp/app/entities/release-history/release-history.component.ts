import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ReleaseHistory } from './release-history.model';
import { ReleaseHistoryService } from './release-history.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-release-history',
    templateUrl: './release-history.component.html'
})
export class ReleaseHistoryComponent implements OnInit, OnDestroy {
releaseHistories: ReleaseHistory[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private releaseHistoryService: ReleaseHistoryService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.releaseHistoryService.query().subscribe(
            (res: HttpResponse<ReleaseHistory[]>) => {
                this.releaseHistories = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInReleaseHistories();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ReleaseHistory) {
        return item.id;
    }
    registerChangeInReleaseHistories() {
        this.eventSubscriber = this.eventManager.subscribe('releaseHistoryListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
