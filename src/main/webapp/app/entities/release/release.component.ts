import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Release } from './release.model';
import { ReleaseService } from './release.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-release',
    templateUrl: './release.component.html'
})
export class ReleaseComponent implements OnInit, OnDestroy {
releases: Release[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private releaseService: ReleaseService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.releaseService.query().subscribe(
            (res: HttpResponse<Release[]>) => {
                this.releases = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInReleases();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Release) {
        return item.id;
    }
    registerChangeInReleases() {
        this.eventSubscriber = this.eventManager.subscribe('releaseListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
