import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Examiner } from './examiner.model';
import { ExaminerPopupService } from './examiner-popup.service';
import { ExaminerService } from './examiner.service';

@Component({
    selector: 'jhi-examiner-dialog',
    templateUrl: './examiner-dialog.component.html'
})
export class ExaminerDialogComponent implements OnInit {

    examiner: Examiner;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private examinerService: ExaminerService,
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
        if (this.examiner.id !== undefined) {
            this.subscribeToSaveResponse(
                this.examinerService.update(this.examiner));
        } else {
            this.subscribeToSaveResponse(
                this.examinerService.create(this.examiner));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Examiner>>) {
        result.subscribe((res: HttpResponse<Examiner>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Examiner) {
        this.eventManager.broadcast({ name: 'examinerListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-examiner-popup',
    template: ''
})
export class ExaminerPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private examinerPopupService: ExaminerPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.examinerPopupService
                    .open(ExaminerDialogComponent as Component, params['id']);
            } else {
                this.examinerPopupService
                    .open(ExaminerDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
