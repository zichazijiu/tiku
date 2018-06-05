import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Examiner } from './examiner.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Examiner>;

@Injectable()
export class ExaminerService {

    private resourceUrl =  SERVER_API_URL + 'api/examiners';

    constructor(private http: HttpClient) { }

    create(examiner: Examiner): Observable<EntityResponseType> {
        const copy = this.convert(examiner);
        return this.http.post<Examiner>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(examiner: Examiner): Observable<EntityResponseType> {
        const copy = this.convert(examiner);
        return this.http.put<Examiner>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Examiner>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Examiner[]>> {
        const options = createRequestOption(req);
        return this.http.get<Examiner[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Examiner[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Examiner = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Examiner[]>): HttpResponse<Examiner[]> {
        const jsonResponse: Examiner[] = res.body;
        const body: Examiner[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Examiner.
     */
    private convertItemFromServer(examiner: Examiner): Examiner {
        const copy: Examiner = Object.assign({}, examiner);
        return copy;
    }

    /**
     * Convert a Examiner to a JSON which can be sent to the server.
     */
    private convert(examiner: Examiner): Examiner {
        const copy: Examiner = Object.assign({}, examiner);
        return copy;
    }
}
