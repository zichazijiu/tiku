import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Release } from './release.model';
import { ReleasePopupService } from './release-popup.service';
import { ReleaseService } from './release.service';

@Component({
    selector: 'jhi-release-dialog',
    templateUrl: './release-dialog.component.html'
})
export class ReleaseDialogComponent implements OnInit {

    release: Release;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private releaseService: ReleaseService,
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
        if (this.release.id !== undefined) {
            this.subscribeToSaveResponse(
                this.releaseService.update(this.release));
        } else {
            this.subscribeToSaveResponse(
                this.releaseService.create(this.release));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Release>>) {
        result.subscribe((res: HttpResponse<Release>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Release) {
        this.eventManager.broadcast({ name: 'releaseListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-release-popup',
    template: ''
})
export class ReleasePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private releasePopupService: ReleasePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.releasePopupService
                    .open(ReleaseDialogComponent as Component, params['id']);
            } else {
                this.releasePopupService
                    .open(ReleaseDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
