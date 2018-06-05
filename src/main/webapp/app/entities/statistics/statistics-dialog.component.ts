import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Statistics } from './statistics.model';
import { StatisticsPopupService } from './statistics-popup.service';
import { StatisticsService } from './statistics.service';

@Component({
    selector: 'jhi-statistics-dialog',
    templateUrl: './statistics-dialog.component.html'
})
export class StatisticsDialogComponent implements OnInit {

    statistics: Statistics;
    isSaving: boolean;
    statisticsDateDp: any;
    updateDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private statisticsService: StatisticsService,
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
        if (this.statistics.id !== undefined) {
            this.subscribeToSaveResponse(
                this.statisticsService.update(this.statistics));
        } else {
            this.subscribeToSaveResponse(
                this.statisticsService.create(this.statistics));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Statistics>>) {
        result.subscribe((res: HttpResponse<Statistics>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Statistics) {
        this.eventManager.broadcast({ name: 'statisticsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-statistics-popup',
    template: ''
})
export class StatisticsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private statisticsPopupService: StatisticsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.statisticsPopupService
                    .open(StatisticsDialogComponent as Component, params['id']);
            } else {
                this.statisticsPopupService
                    .open(StatisticsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
