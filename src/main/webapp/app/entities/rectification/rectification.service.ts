import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Rectification } from './rectification.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Rectification>;

@Injectable()
export class RectificationService {

    private resourceUrl =  SERVER_API_URL + 'api/rectifications';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(rectification: Rectification): Observable<EntityResponseType> {
        const copy = this.convert(rectification);
        return this.http.post<Rectification>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(rectification: Rectification): Observable<EntityResponseType> {
        const copy = this.convert(rectification);
        return this.http.put<Rectification>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Rectification>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Rectification[]>> {
        const options = createRequestOption(req);
        return this.http.get<Rectification[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Rectification[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Rectification = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Rectification[]>): HttpResponse<Rectification[]> {
        const jsonResponse: Rectification[] = res.body;
        const body: Rectification[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Rectification.
     */
    private convertItemFromServer(rectification: Rectification): Rectification {
        const copy: Rectification = Object.assign({}, rectification);
        copy.rectificationTime = this.dateUtils
            .convertDateTimeFromServer(rectification.rectificationTime);
        return copy;
    }

    /**
     * Convert a Rectification to a JSON which can be sent to the server.
     */
    private convert(rectification: Rectification): Rectification {
        const copy: Rectification = Object.assign({}, rectification);

        copy.rectificationTime = this.dateUtils.toDate(rectification.rectificationTime);
        return copy;
    }
}
