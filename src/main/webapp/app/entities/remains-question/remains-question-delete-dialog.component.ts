import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { RemainsQuestion } from './remains-question.model';
import { RemainsQuestionPopupService } from './remains-question-popup.service';
import { RemainsQuestionService } from './remains-question.service';

@Component({
    selector: 'jhi-remains-question-delete-dialog',
    templateUrl: './remains-question-delete-dialog.component.html'
})
export class RemainsQuestionDeleteDialogComponent {

    remainsQuestion: RemainsQuestion;

    constructor(
        private remainsQuestionService: RemainsQuestionService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.remainsQuestionService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'remainsQuestionListModification',
                content: 'Deleted an remainsQuestion'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-remains-question-delete-popup',
    template: ''
})
export class RemainsQuestionDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private remainsQuestionPopupService: RemainsQuestionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.remainsQuestionPopupService
                .open(RemainsQuestionDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
