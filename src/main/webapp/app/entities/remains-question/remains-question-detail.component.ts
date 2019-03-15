import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRemainsQuestion } from 'app/shared/model/remains-question.model';

@Component({
  selector: 'jhi-remains-question-detail',
  templateUrl: './remains-question-detail.component.html'
})
export class RemainsQuestionDetailComponent implements OnInit {
  remainsQuestion: IRemainsQuestion;

  constructor(private activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ remainsQuestion }) => {
      this.remainsQuestion = remainsQuestion;
    });
  }

  previousState() {
    window.history.back();
  }
}
