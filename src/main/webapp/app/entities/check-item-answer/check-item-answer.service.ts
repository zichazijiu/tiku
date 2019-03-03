import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { CheckItemAnswer } from './check-item-answer.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<CheckItemAnswer>;

@Injectable()
export class CheckItemAnswerService {

    private resourceUrl =  SERVER_API_URL + 'api/check-item-answers';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(checkItemAnswer: CheckItemAnswer): Observable<EntityResponseType> {
        const copy = this.convert(checkItemAnswer);
        return this.http.post<CheckItemAnswer>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(checkItemAnswer: CheckItemAnswer): Observable<EntityResponseType> {
        const copy = this.convert(checkItemAnswer);
        return this.http.put<CheckItemAnswer>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<CheckItemAnswer>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<CheckItemAnswer[]>> {
        const options = createRequestOption(req);
        return this.http.get<CheckItemAnswer[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<CheckItemAnswer[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: CheckItemAnswer = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<CheckItemAnswer[]>): HttpResponse<CheckItemAnswer[]> {
        const jsonResponse: CheckItemAnswer[] = res.body;
        const body: CheckItemAnswer[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to CheckItemAnswer.
     */
    private convertItemFromServer(checkItemAnswer: CheckItemAnswer): CheckItemAnswer {
        const copy: CheckItemAnswer = Object.assign({}, checkItemAnswer);
        copy.createdDate = this.dateUtils
            .convertLocalDateFromServer(checkItemAnswer.createdDate);
        return copy;
    }

    /**
     * Convert a CheckItemAnswer to a JSON which can be sent to the server.
     */
    private convert(checkItemAnswer: CheckItemAnswer): CheckItemAnswer {
        const copy: CheckItemAnswer = Object.assign({}, checkItemAnswer);
        copy.createdDate = this.dateUtils
            .convertLocalDateToServer(checkItemAnswer.createdDate);
        return copy;
    }
}
