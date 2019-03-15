import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IRemainsQuestion } from 'app/shared/model/remains-question.model';
import { Principal } from 'app/core';
import { RemainsQuestionService } from './remains-question.service';

@Component({
  selector: 'jhi-remains-question',
  templateUrl: './remains-question.component.html'
})
export class RemainsQuestionComponent implements OnInit, OnDestroy {
  remainsQuestions: IRemainsQuestion[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    private remainsQuestionService: RemainsQuestionService,
    private jhiAlertService: JhiAlertService,
    private eventManager: JhiEventManager,
    private principal: Principal
  ) {}

  loadAll() {
    this.remainsQuestionService.query().subscribe(
      (res: HttpResponse<IRemainsQuestion[]>) => {
        this.remainsQuestions = res.body;
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  ngOnInit() {
    this.loadAll();
    this.principal.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInRemainsQuestions();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IRemainsQuestion) {
    return item.id;
  }

  registerChangeInRemainsQuestions() {
    this.eventSubscriber = this.eventManager.subscribe('remainsQuestionListModification', response => this.loadAll());
  }

  private onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
