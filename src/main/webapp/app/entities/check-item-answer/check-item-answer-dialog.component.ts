import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CheckItemAnswer } from './check-item-answer.model';
import { CheckItemAnswerPopupService } from './check-item-answer-popup.service';
import { CheckItemAnswerService } from './check-item-answer.service';

@Component({
    selector: 'jhi-check-item-answer-dialog',
    templateUrl: './check-item-answer-dialog.component.html'
})
export class CheckItemAnswerDialogComponent implements OnInit {

    checkItemAnswer: CheckItemAnswer;
    isSaving: boolean;
    createdDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private checkItemAnswerService: CheckItemAnswerService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.checkItemAnswer.id !== undefined) {
            this.subscribeToSaveResponse(
                this.checkItemAnswerService.update(this.checkItemAnswer));
        } else {
            this.subscribeToSaveResponse(
                this.checkItemAnswerService.create(this.checkItemAnswer));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<CheckItemAnswer>>) {
        result.subscribe((res: HttpResponse<CheckItemAnswer>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: CheckItemAnswer) {
        this.eventManager.broadcast({ name: 'checkItemAnswerListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-check-item-answer-popup',
    template: ''
})
export class CheckItemAnswerPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private checkItemAnswerPopupService: CheckItemAnswerPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.checkItemAnswerPopupService
                    .open(CheckItemAnswerDialogComponent as Component, params['id']);
            } else {
                this.checkItemAnswerPopupService
                    .open(CheckItemAnswerDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
