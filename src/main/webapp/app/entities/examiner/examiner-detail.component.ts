import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Examiner } from './examiner.model';
import { ExaminerService } from './examiner.service';

@Component({
    selector: 'jhi-examiner-detail',
    templateUrl: './examiner-detail.component.html'
})
export class ExaminerDetailComponent implements OnInit, OnDestroy {

    examiner: Examiner;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private examinerService: ExaminerService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInExaminers();
    }

    load(id) {
        this.examinerService.find(id)
            .subscribe((examinerResponse: HttpResponse<Examiner>) => {
                this.examiner = examinerResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInExaminers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'examinerListModification',
            (response) => this.load(this.examiner.id)
        );
    }
}
