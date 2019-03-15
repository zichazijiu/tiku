import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IRectification } from 'app/shared/model/rectification.model';
import { RectificationService } from './rectification.service';

@Component({
  selector: 'jhi-rectification-update',
  templateUrl: './rectification-update.component.html'
})
export class RectificationUpdateComponent implements OnInit {
  private _rectification: IRectification;
  isSaving: boolean;
  rectificationTime: string;

  constructor(private rectificationService: RectificationService, private activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ rectification }) => {
      this.rectification = rectification;
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    this.rectification.rectificationTime = moment(this.rectificationTime, DATE_TIME_FORMAT);
    if (this.rectification.id !== undefined) {
      this.subscribeToSaveResponse(this.rectificationService.update(this.rectification));
    } else {
      this.subscribeToSaveResponse(this.rectificationService.create(this.rectification));
    }
  }

  private subscribeToSaveResponse(result: Observable<HttpResponse<IRectification>>) {
    result.subscribe((res: HttpResponse<IRectification>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  private onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  private onSaveError() {
    this.isSaving = false;
  }
  get rectification() {
    return this._rectification;
  }

  set rectification(rectification: IRectification) {
    this._rectification = rectification;
    this.rectificationTime = moment(rectification.rectificationTime).format(DATE_TIME_FORMAT);
  }
}
