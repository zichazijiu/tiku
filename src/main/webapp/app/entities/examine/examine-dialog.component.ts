import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Examine } from './examine.model';
import { ExaminePopupService } from './examine-popup.service';
import { ExamineService } from './examine.service';

@Component({
    selector: 'jhi-examine-dialog',
    templateUrl: './examine-dialog.component.html'
})
export class ExamineDialogComponent implements OnInit {

    examine: Examine;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private examineService: ExamineService,
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
        if (this.examine.id !== undefined) {
            this.subscribeToSaveResponse(
                this.examineService.update(this.examine));
        } else {
            this.subscribeToSaveResponse(
                this.examineService.create(this.examine));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Examine>>) {
        result.subscribe((res: HttpResponse<Examine>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Examine) {
        this.eventManager.broadcast({ name: 'examineListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-examine-popup',
    template: ''
})
export class ExaminePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private examinePopupService: ExaminePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.examinePopupService
                    .open(ExamineDialogComponent as Component, params['id']);
            } else {
                this.examinePopupService
                    .open(ExamineDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
