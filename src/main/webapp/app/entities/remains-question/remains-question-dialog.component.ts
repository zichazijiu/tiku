import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { RemainsQuestion } from './remains-question.model';
import { RemainsQuestionPopupService } from './remains-question-popup.service';
import { RemainsQuestionService } from './remains-question.service';
import { ReportItems, ReportItemsService } from '../report-items';
import { Rectification, RectificationService } from '../rectification';

@Component({
    selector: 'jhi-remains-question-dialog',
    templateUrl: './remains-question-dialog.component.html'
})
export class RemainsQuestionDialogComponent implements OnInit {

    remainsQuestion: RemainsQuestion;
    isSaving: boolean;

    reportitems: ReportItems[];

    rectifications: Rectification[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private remainsQuestionService: RemainsQuestionService,
        private reportItemsService: ReportItemsService,
        private rectificationService: RectificationService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.reportItemsService.query()
            .subscribe((res: HttpResponse<ReportItems[]>) => { this.reportitems = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.rectificationService
            .query({filter: 'remainsquestion-is-null'})
            .subscribe((res: HttpResponse<Rectification[]>) => {
                if (!this.remainsQuestion.rectification || !this.remainsQuestion.rectification.id) {
                    this.rectifications = res.body;
                } else {
                    this.rectificationService
                        .find(this.remainsQuestion.rectification.id)
                        .subscribe((subRes: HttpResponse<Rectification>) => {
                            this.rectifications = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.remainsQuestion.id !== undefined) {
            this.subscribeToSaveResponse(
                this.remainsQuestionService.update(this.remainsQuestion));
        } else {
            this.subscribeToSaveResponse(
                this.remainsQuestionService.create(this.remainsQuestion));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<RemainsQuestion>>) {
        result.subscribe((res: HttpResponse<RemainsQuestion>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: RemainsQuestion) {
        this.eventManager.broadcast({ name: 'remainsQuestionListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackReportItemsById(index: number, item: ReportItems) {
        return item.id;
    }

    trackRectificationById(index: number, item: Rectification) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-remains-question-popup',
    template: ''
})
export class RemainsQuestionPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private remainsQuestionPopupService: RemainsQuestionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.remainsQuestionPopupService
                    .open(RemainsQuestionDialogComponent as Component, params['id']);
            } else {
                this.remainsQuestionPopupService
                    .open(RemainsQuestionDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
