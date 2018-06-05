import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Subject } from './subject.model';
import { SubjectService } from './subject.service';

@Component({
    selector: 'jhi-subject-detail',
    templateUrl: './subject-detail.component.html'
})
export class SubjectDetailComponent implements OnInit, OnDestroy {

    subject: Subject;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private subjectService: SubjectService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSubjects();
    }

    load(id) {
        this.subjectService.find(id)
            .subscribe((subjectResponse: HttpResponse<Subject>) => {
                this.subject = subjectResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSubjects() {
        this.eventSubscriber = this.eventManager.subscribe(
            'subjectListModification',
            (response) => this.load(this.subject.id)
        );
    }
}
