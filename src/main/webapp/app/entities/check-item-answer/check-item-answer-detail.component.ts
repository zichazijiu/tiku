import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { CheckItemAnswer } from './check-item-answer.model';
import { CheckItemAnswerService } from './check-item-answer.service';

@Component({
    selector: 'jhi-check-item-answer-detail',
    templateUrl: './check-item-answer-detail.component.html'
})
export class CheckItemAnswerDetailComponent implements OnInit, OnDestroy {

    checkItemAnswer: CheckItemAnswer;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private checkItemAnswerService: CheckItemAnswerService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCheckItemAnswers();
    }

    load(id) {
        this.checkItemAnswerService.find(id)
            .subscribe((checkItemAnswerResponse: HttpResponse<CheckItemAnswer>) => {
                this.checkItemAnswer = checkItemAnswerResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCheckItemAnswers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'checkItemAnswerListModification',
            (response) => this.load(this.checkItemAnswer.id)
        );
    }
}
