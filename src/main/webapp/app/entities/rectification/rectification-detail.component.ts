import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Rectification } from './rectification.model';
import { RectificationService } from './rectification.service';

@Component({
    selector: 'jhi-rectification-detail',
    templateUrl: './rectification-detail.component.html'
})
export class RectificationDetailComponent implements OnInit, OnDestroy {

    rectification: Rectification;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private rectificationService: RectificationService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInRectifications();
    }

    load(id) {
        this.rectificationService.find(id)
            .subscribe((rectificationResponse: HttpResponse<Rectification>) => {
                this.rectification = rectificationResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInRectifications() {
        this.eventSubscriber = this.eventManager.subscribe(
            'rectificationListModification',
            (response) => this.load(this.rectification.id)
        );
    }
}
