import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Statistics } from './statistics.model';
import { StatisticsPopupService } from './statistics-popup.service';
import { StatisticsService } from './statistics.service';

@Component({
    selector: 'jhi-statistics-delete-dialog',
    templateUrl: './statistics-delete-dialog.component.html'
})
export class StatisticsDeleteDialogComponent {

    statistics: Statistics;

    constructor(
        private statisticsService: StatisticsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.statisticsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'statisticsListModification',
                content: 'Deleted an statistics'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-statistics-delete-popup',
    template: ''
})
export class StatisticsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private statisticsPopupService: StatisticsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.statisticsPopupService
                .open(StatisticsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
