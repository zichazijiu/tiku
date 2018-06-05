import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Statistics } from './statistics.model';
import { StatisticsService } from './statistics.service';

@Component({
    selector: 'jhi-statistics-detail',
    templateUrl: './statistics-detail.component.html'
})
export class StatisticsDetailComponent implements OnInit, OnDestroy {

    statistics: Statistics;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private statisticsService: StatisticsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInStatistics();
    }

    load(id) {
        this.statisticsService.find(id)
            .subscribe((statisticsResponse: HttpResponse<Statistics>) => {
                this.statistics = statisticsResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInStatistics() {
        this.eventSubscriber = this.eventManager.subscribe(
            'statisticsListModification',
            (response) => this.load(this.statistics.id)
        );
    }
}
