import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ReportItems } from './report-items.model';
import { ReportItemsPopupService } from './report-items-popup.service';
import { ReportItemsService } from './report-items.service';
import { Report, ReportService } from '../report';
import { CheckItem, CheckItemService } from '../check-item';

@Component({
    selector: 'jhi-report-items-dialog',
    templateUrl: './report-items-dialog.component.html'
})
export class ReportItemsDialogComponent implements OnInit {

    reportItems: ReportItems;
    isSaving: boolean;

    reports: Report[];

    checkitems: CheckItem[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private reportItemsService: ReportItemsService,
        private reportService: ReportService,
        private checkItemService: CheckItemService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.reportService.query()
            .subscribe((res: HttpResponse<Report[]>) => { this.reports = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.checkItemService
            .query({filter: 'reportitems-is-null'})
            .subscribe((res: HttpResponse<CheckItem[]>) => {
                if (!this.reportItems.checkItem || !this.reportItems.checkItem.id) {
                    this.checkitems = res.body;
                } else {
                    this.checkItemService
                        .find(this.reportItems.checkItem.id)
                        .subscribe((subRes: HttpResponse<CheckItem>) => {
                            this.checkitems = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.reportItems.id !== undefined) {
            this.subscribeToSaveResponse(
                this.reportItemsService.update(this.reportItems));
        } else {
            this.subscribeToSaveResponse(
                this.reportItemsService.create(this.reportItems));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ReportItems>>) {
        result.subscribe((res: HttpResponse<ReportItems>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: ReportItems) {
        this.eventManager.broadcast({ name: 'reportItemsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackReportById(index: number, item: Report) {
        return item.id;
    }

    trackCheckItemById(index: number, item: CheckItem) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-report-items-popup',
    template: ''
})
export class ReportItemsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private reportItemsPopupService: ReportItemsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.reportItemsPopupService
                    .open(ReportItemsDialogComponent as Component, params['id']);
            } else {
                this.reportItemsPopupService
                    .open(ReportItemsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
