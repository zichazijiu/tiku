import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ReleaseHistory } from './release-history.model';
import { ReleaseHistoryPopupService } from './release-history-popup.service';
import { ReleaseHistoryService } from './release-history.service';

@Component({
    selector: 'jhi-release-history-dialog',
    templateUrl: './release-history-dialog.component.html'
})
export class ReleaseHistoryDialogComponent implements OnInit {

    releaseHistory: ReleaseHistory;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private releaseHistoryService: ReleaseHistoryService,
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
        if (this.releaseHistory.id !== undefined) {
            this.subscribeToSaveResponse(
                this.releaseHistoryService.update(this.releaseHistory));
        } else {
            this.subscribeToSaveResponse(
                this.releaseHistoryService.create(this.releaseHistory));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ReleaseHistory>>) {
        result.subscribe((res: HttpResponse<ReleaseHistory>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: ReleaseHistory) {
        this.eventManager.broadcast({ name: 'releaseHistoryListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-release-history-popup',
    template: ''
})
export class ReleaseHistoryPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private releaseHistoryPopupService: ReleaseHistoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.releaseHistoryPopupService
                    .open(ReleaseHistoryDialogComponent as Component, params['id']);
            } else {
                this.releaseHistoryPopupService
                    .open(ReleaseHistoryDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
