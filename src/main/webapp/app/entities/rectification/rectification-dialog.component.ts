import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Rectification } from './rectification.model';
import { RectificationPopupService } from './rectification-popup.service';
import { RectificationService } from './rectification.service';

@Component({
    selector: 'jhi-rectification-dialog',
    templateUrl: './rectification-dialog.component.html'
})
export class RectificationDialogComponent implements OnInit {

    rectification: Rectification;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private rectificationService: RectificationService,
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
