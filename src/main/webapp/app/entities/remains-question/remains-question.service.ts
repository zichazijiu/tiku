import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { RemainsQuestion } from './remains-question.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<RemainsQuestion>;

@Injectable()
export class RemainsQuestionService {

    private resourceUrl =  SERVER_API_URL + 'api/remains-questions';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(remainsQuestion: RemainsQuestion): Observable<EntityResponseType> {
        const copy = this.convert(remainsQuestion);
        return this.http.post<RemainsQuestion>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(remainsQuestion: RemainsQuestion): Observable<EntityResponseType> {
        const copy = this.convert(remainsQuestion);
        return this.http.put<RemainsQuestion>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<RemainsQuestion>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<RemainsQuestion[]>> {
        const options = createRequestOption(req);
        return this.http.get<RemainsQuestion[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<RemainsQuestion[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: RemainsQuestion = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<RemainsQuestion[]>): HttpResponse<RemainsQuestion[]> {
        const jsonResponse: RemainsQuestion[] = res.body;
        const body: RemainsQuestion[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to RemainsQuestion.
     */
    private convertItemFromServer(remainsQuestion: RemainsQuestion): RemainsQuestion {
        const copy: RemainsQuestion = Object.assign({}, remainsQuestion);
        copy.createdTime = this.dateUtils
            .convertDateTimeFromServer(remainsQuestion.createdTime);
        return copy;
    }

    /**
     * Convert a RemainsQuestion to a JSON which can be sent to the server.
     */
    private convert(remainsQuestion: RemainsQuestion): RemainsQuestion {
        const copy: RemainsQuestion = Object.assign({}, remainsQuestion);

        copy.createdTime = this.dateUtils.toDate(remainsQuestion.createdTime);
        return copy;
    }
}
