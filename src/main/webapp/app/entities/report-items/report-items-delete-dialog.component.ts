import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ReportItems } from './report-items.model';
import { ReportItemsPopupService } from './report-items-popup.service';
import { ReportItemsService } from './report-items.service';

@Component({
    selector: 'jhi-report-items-delete-dialog',
    templateUrl: './report-items-delete-dialog.component.html'
})
export class ReportItemsDeleteDialogComponent {

    reportItems: ReportItems;

    constructor(
        private reportItemsService: ReportItemsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.reportItemsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'reportItemsListModification',
                content: 'Deleted an reportItems'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-report-items-delete-popup',
    template: ''
})
export class ReportItemsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private reportItemsPopupService: ReportItemsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.reportItemsPopupService
                .open(ReportItemsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
