import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Examine } from './examine.model';
import { ExamineService } from './examine.service';

@Component({
    selector: 'jhi-examine-detail',
    templateUrl: './examine-detail.component.html'
})
export class ExamineDetailComponent implements OnInit, OnDestroy {

    examine: Examine;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private examineService: ExamineService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInExamines();
    }

    load(id) {
        this.examineService.find(id)
            .subscribe((examineResponse: HttpResponse<Examine>) => {
                this.examine = examineResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInExamines() {
        this.eventSubscriber = this.eventManager.subscribe(
            'examineListModification',
            (response) => this.load(this.examine.id)
        );
    }
}
