import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { RemainsQuestion } from './remains-question.model';
import { RemainsQuestionService } from './remains-question.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-remains-question',
    templateUrl: './remains-question.component.html'
})
export class RemainsQuestionComponent implements OnInit, OnDestroy {
remainsQuestions: RemainsQuestion[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private remainsQuestionService: RemainsQuestionService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.remainsQuestionService.query().subscribe(
            (res: HttpResponse<RemainsQuestion[]>) => {
                this.remainsQuestions = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInRemainsQuestions();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: RemainsQuestion) {
        return item.id;
    }
    registerChangeInRemainsQuestions() {
        this.eventSubscriber = this.eventManager.subscribe('remainsQuestionListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
