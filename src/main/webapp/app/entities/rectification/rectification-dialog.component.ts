import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Rectification } from './rectification.model';
import { RectificationPopupService } from './rectification-popup.service';
import { RectificationService } from './rectification.service';
import { RemainsQuestion, RemainsQuestionService } from '../remains-question';

@Component({
    selector: 'jhi-rectification-dialog',
    templateUrl: './rectification-dialog.component.html'
})
export class RectificationDialogComponent implements OnInit {

    rectification: Rectification;
    isSaving: boolean;

    remainsquestions: RemainsQuestion[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private rectificationService: RectificationService,
        private remainsQuestionService: RemainsQuestionService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.remainsQuestionService
            .query({filter: 'rectification-is-null'})
            .subscribe((res: HttpResponse<RemainsQuestion[]>) => {
                if (!this.rectification.remainsQuestion || !this.rectification.remainsQuestion.id) {
                    this.remainsquestions = res.body;
                } else {
                    this.remainsQuestionService
                        .find(this.rectification.remainsQuestion.id)
                        .subscribe((subRes: HttpResponse<RemainsQuestion>) => {
                            this.remainsquestions = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.rectification.id !== undefined) {
            this.subscribeToSaveResponse(
                this.rectificationService.update(this.rectification));
        } else {
            this.subscribeToSaveResponse(
                this.rectificationService.create(this.rectification));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Rectification>>) {
        result.subscribe((res: HttpResponse<Rectification>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Rectification) {
        this.eventManager.broadcast({ name: 'rectificationListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackRemainsQuestionById(index: number, item: RemainsQuestion) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-rectification-popup',
    template: ''
})
export class RectificationPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private rectificationPopupService: RectificationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.rectificationPopupService
                    .open(RectificationDialogComponent as Component, params['id']);
            } else {
                this.rectificationPopupService
                    .open(RectificationDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
