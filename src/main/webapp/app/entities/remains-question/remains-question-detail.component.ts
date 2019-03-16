import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { RemainsQuestion } from './remains-question.model';
import { RemainsQuestionService } from './remains-question.service';

@Component({
    selector: 'jhi-remains-question-detail',
    templateUrl: './remains-question-detail.component.html'
})
export class RemainsQuestionDetailComponent implements OnInit, OnDestroy {

    remainsQuestion: RemainsQuestion;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private remainsQuestionService: RemainsQuestionService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInRemainsQuestions();
    }

    load(id) {
        this.remainsQuestionService.find(id)
            .subscribe((remainsQuestionResponse: HttpResponse<RemainsQuestion>) => {
                this.remainsQuestion = remainsQuestionResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInRemainsQuestions() {
        this.eventSubscriber = this.eventManager.subscribe(
            'remainsQuestionListModification',
            (response) => this.load(this.remainsQuestion.id)
        );
    }
}
