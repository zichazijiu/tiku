import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRemainsQuestion } from 'app/shared/model/remains-question.model';

type EntityResponseType = HttpResponse<IRemainsQuestion>;
type EntityArrayResponseType = HttpResponse<IRemainsQuestion[]>;

@Injectable({ providedIn: 'root' })
export class RemainsQuestionService {
  private resourceUrl = SERVER_API_URL + 'api/remains-questions';

  constructor(private http: HttpClient) {}

  create(remainsQuestion: IRemainsQuestion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(remainsQuestion);
    return this.http
      .post<IRemainsQuestion>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(remainsQuestion: IRemainsQuestion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(remainsQuestion);
    return this.http
      .put<IRemainsQuestion>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRemainsQuestion>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRemainsQuestion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  private convertDateFromClient(remainsQuestion: IRemainsQuestion): IRemainsQuestion {
    const copy: IRemainsQuestion = Object.assign({}, remainsQuestion, {
      createdTime:
        remainsQuestion.createdTime != null && remainsQuestion.createdTime.isValid() ? remainsQuestion.createdTime.toJSON() : null
    });
    return copy;
  }

  private convertDateFromServer(res: EntityResponseType): EntityResponseType {
    res.body.createdTime = res.body.createdTime != null ? moment(res.body.createdTime) : null;
    return res;
  }

  private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    res.body.forEach((remainsQuestion: IRemainsQuestion) => {
      remainsQuestion.createdTime = remainsQuestion.createdTime != null ? moment(remainsQuestion.createdTime) : null;
    });
    return res;
  }
}
