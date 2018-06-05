import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Subject } from './subject.model';
import { SubjectPopupService } from './subject-popup.service';
import { SubjectService } from './subject.service';

@Component({
    selector: 'jhi-subject-delete-dialog',
    templateUrl: './subject-delete-dialog.component.html'
})
export class SubjectDeleteDialogComponent {

    subject: Subject;

    constructor(
        private subjectService: SubjectService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.subjectService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'subjectListModification',
                content: 'Deleted an subject'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-subject-delete-popup',
    template: ''
})
export class SubjectDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private subjectPopupService: SubjectPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.subjectPopupService
                .open(SubjectDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
