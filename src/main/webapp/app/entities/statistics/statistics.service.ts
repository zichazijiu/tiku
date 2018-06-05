import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Statistics } from './statistics.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Statistics>;

@Injectable()
export class StatisticsService {

    private resourceUrl =  SERVER_API_URL + 'api/statistics';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(statistics: Statistics): Observable<EntityResponseType> {
        const copy = this.convert(statistics);
        return this.http.post<Statistics>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(statistics: Statistics): Observable<EntityResponseType> {
        const copy = this.convert(statistics);
        return this.http.put<Statistics>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Statistics>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Statistics[]>> {
        const options = createRequestOption(req);
        return this.http.get<Statistics[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Statistics[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Statistics = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Statistics[]>): HttpResponse<Statistics[]> {
        const jsonResponse: Statistics[] = res.body;
        const body: Statistics[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Statistics.
     */
    private convertItemFromServer(statistics: Statistics): Statistics {
        const copy: Statistics = Object.assign({}, statistics);
        copy.statisticsDate = this.dateUtils
            .convertLocalDateFromServer(statistics.statisticsDate);
        copy.updateDate = this.dateUtils
            .convertLocalDateFromServer(statistics.updateDate);
        return copy;
    }

    /**
     * Convert a Statistics to a JSON which can be sent to the server.
     */
    private convert(statistics: Statistics): Statistics {
        const copy: Statistics = Object.assign({}, statistics);
        copy.statisticsDate = this.dateUtils
            .convertLocalDateToServer(statistics.statisticsDate);
        copy.updateDate = this.dateUtils
            .convertLocalDateToServer(statistics.updateDate);
        return copy;
    }
}
