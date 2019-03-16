import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Rectification } from './rectification.model';
import { RectificationPopupService } from './rectification-popup.service';
import { RectificationService } from './rectification.service';

@Component({
    selector: 'jhi-rectification-delete-dialog',
    templateUrl: './rectification-delete-dialog.component.html'
})
export class RectificationDeleteDialogComponent {

    rectification: Rectification;

    constructor(
        private rectificationService: RectificationService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.rectificationService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'rectificationListModification',
                content: 'Deleted an rectification'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-rectification-delete-popup',
    template: ''
})
export class RectificationDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private rectificationPopupService: RectificationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.rectificationPopupService
                .open(RectificationDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
