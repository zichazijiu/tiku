import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CheckItemAnswer } from './check-item-answer.model';
import { CheckItemAnswerPopupService } from './check-item-answer-popup.service';
import { CheckItemAnswerService } from './check-item-answer.service';

@Component({
    selector: 'jhi-check-item-answer-delete-dialog',
    templateUrl: './check-item-answer-delete-dialog.component.html'
})
export class CheckItemAnswerDeleteDialogComponent {

    checkItemAnswer: CheckItemAnswer;

    constructor(
        private checkItemAnswerService: CheckItemAnswerService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.checkItemAnswerService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'checkItemAnswerListModification',
                content: 'Deleted an checkItemAnswer'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-check-item-answer-delete-popup',
    template: ''
})
export class CheckItemAnswerDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private checkItemAnswerPopupService: CheckItemAnswerPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.checkItemAnswerPopupService
                .open(CheckItemAnswerDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
