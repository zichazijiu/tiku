import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Examine } from './examine.model';
import { ExaminePopupService } from './examine-popup.service';
import { ExamineService } from './examine.service';

@Component({
    selector: 'jhi-examine-delete-dialog',
    templateUrl: './examine-delete-dialog.component.html'
})
export class ExamineDeleteDialogComponent {

    examine: Examine;

    constructor(
        private examineService: ExamineService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.examineService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'examineListModification',
                content: 'Deleted an examine'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-examine-delete-popup',
    template: ''
})
export class ExamineDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private examinePopupService: ExaminePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.examinePopupService
                .open(ExamineDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
