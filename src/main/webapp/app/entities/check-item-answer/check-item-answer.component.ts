import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { CheckItemAnswer } from './check-item-answer.model';
import { CheckItemAnswerService } from './check-item-answer.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-check-item-answer',
    templateUrl: './check-item-answer.component.html'
})
export class CheckItemAnswerComponent implements OnInit, OnDestroy {
checkItemAnswers: CheckItemAnswer[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private checkItemAnswerService: CheckItemAnswerService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.checkItemAnswerService.query().subscribe(
            (res: HttpResponse<CheckItemAnswer[]>) => {
                this.checkItemAnswers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInCheckItemAnswers();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: CheckItemAnswer) {
        return item.id;
    }
    registerChangeInCheckItemAnswers() {
        this.eventSubscriber = this.eventManager.subscribe('checkItemAnswerListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
