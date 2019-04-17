import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ReleaseHistory } from './release-history.model';
import { ReleaseHistoryPopupService } from './release-history-popup.service';
import { ReleaseHistoryService } from './release-history.service';

@Component({
    selector: 'jhi-release-history-delete-dialog',
    templateUrl: './release-history-delete-dialog.component.html'
})
export class ReleaseHistoryDeleteDialogComponent {

    releaseHistory: ReleaseHistory;

    constructor(
        private releaseHistoryService: ReleaseHistoryService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.releaseHistoryService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'releaseHistoryListModification',
                content: 'Deleted an releaseHistory'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-release-history-delete-popup',
    template: ''
})
export class ReleaseHistoryDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private releaseHistoryPopupService: ReleaseHistoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.releaseHistoryPopupService
                .open(ReleaseHistoryDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
