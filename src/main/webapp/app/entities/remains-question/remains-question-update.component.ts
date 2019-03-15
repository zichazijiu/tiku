import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IRemainsQuestion } from 'app/shared/model/remains-question.model';
import { RemainsQuestionService } from './remains-question.service';

@Component({
  selector: 'jhi-remains-question-update',
  templateUrl: './remains-question-update.component.html'
})
export class RemainsQuestionUpdateComponent implements OnInit {
  private _remainsQuestion: IRemainsQuestion;
  isSaving: boolean;
  createdTime: string;

  constructor(private remainsQuestionService: RemainsQuestionService, private activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ remainsQuestion }) => {
      this.remainsQuestion = remainsQuestion;
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    this.remainsQuestion.createdTime = moment(this.createdTime, DATE_TIME_FORMAT);
    if (this.remainsQuestion.id !== undefined) {
      this.subscribeToSaveResponse(this.remainsQuestionService.update(this.remainsQuestion));
    } else {
      this.subscribeToSaveResponse(this.remainsQuestionService.create(this.remainsQuestion));
    }
  }

  private subscribeToSaveResponse(result: Observable<HttpResponse<IRemainsQuestion>>) {
    result.subscribe((res: HttpResponse<IRemainsQuestion>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  private onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  private onSaveError() {
    this.isSaving = false;
  }
  get remainsQuestion() {
    return this._remainsQuestion;
  }

  set remainsQuestion(remainsQuestion: IRemainsQuestion) {
    this._remainsQuestion = remainsQuestion;
    this.createdTime = moment(remainsQuestion.createdTime).format(DATE_TIME_FORMAT);
  }
}
