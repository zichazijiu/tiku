import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRectification } from 'app/shared/model/rectification.model';

type EntityResponseType = HttpResponse<IRectification>;
type EntityArrayResponseType = HttpResponse<IRectification[]>;

@Injectable({ providedIn: 'root' })
export class RectificationService {
  private resourceUrl = SERVER_API_URL + 'api/rectifications';

  constructor(private http: HttpClient) {}

  create(rectification: IRectification): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rectification);
    return this.http
      .post<IRectification>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(rectification: IRectification): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rectification);
    return this.http
      .put<IRectification>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRectification>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRectification[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  private convertDateFromClient(rectification: IRectification): IRectification {
    const copy: IRectification = Object.assign({}, rectification, {
      rectificationTime:
        rectification.rectificationTime != null && rectification.rectificationTime.isValid()
          ? rectification.rectificationTime.toJSON()
          : null
    });
    return copy;
  }

  private convertDateFromServer(res: EntityResponseType): EntityResponseType {
    res.body.rectificationTime = res.body.rectificationTime != null ? moment(res.body.rectificationTime) : null;
    return res;
  }

  private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    res.body.forEach((rectification: IRectification) => {
      rectification.rectificationTime = rectification.rectificationTime != null ? moment(rectification.rectificationTime) : null;
    });
    return res;
  }
}
