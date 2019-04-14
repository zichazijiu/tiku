import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Release } from './release.model';
import { ReleasePopupService } from './release-popup.service';
import { ReleaseService } from './release.service';

@Component({
    selector: 'jhi-release-delete-dialog',
    templateUrl: './release-delete-dialog.component.html'
})
export class ReleaseDeleteDialogComponent {

    release: Release;

    constructor(
        private releaseService: ReleaseService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.releaseService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'releaseListModification',
                content: 'Deleted an release'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-release-delete-popup',
    template: ''
})
export class ReleaseDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private releasePopupService: ReleasePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.releasePopupService
                .open(ReleaseDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
