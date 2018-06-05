import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Examiner } from './examiner.model';
import { ExaminerPopupService } from './examiner-popup.service';
import { ExaminerService } from './examiner.service';

@Component({
    selector: 'jhi-examiner-delete-dialog',
    templateUrl: './examiner-delete-dialog.component.html'
})
export class ExaminerDeleteDialogComponent {

    examiner: Examiner;

    constructor(
        private examinerService: ExaminerService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.examinerService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'examinerListModification',
                content: 'Deleted an examiner'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-examiner-delete-popup',
    template: ''
})
export class ExaminerDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private examinerPopupService: ExaminerPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.examinerPopupService
                .open(ExaminerDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
