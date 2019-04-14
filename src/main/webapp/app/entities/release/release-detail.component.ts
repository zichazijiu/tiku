import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Release } from './release.model';
import { ReleaseService } from './release.service';

@Component({
    selector: 'jhi-release-detail',
    templateUrl: './release-detail.component.html'
})
export class ReleaseDetailComponent implements OnInit, OnDestroy {

    release: Release;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private releaseService: ReleaseService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInReleases();
    }

    load(id) {
        this.releaseService.find(id)
            .subscribe((releaseResponse: HttpResponse<Release>) => {
                this.release = releaseResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInReleases() {
        this.eventSubscriber = this.eventManager.subscribe(
            'releaseListModification',
            (response) => this.load(this.release.id)
        );
    }
}
