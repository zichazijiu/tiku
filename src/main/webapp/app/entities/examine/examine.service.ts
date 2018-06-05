import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Examine } from './examine.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Examine>;

@Injectable()
export class ExamineService {

    private resourceUrl =  SERVER_API_URL + 'api/examines';

    constructor(private http: HttpClient) { }

    create(examine: Examine): Observable<EntityResponseType> {
        const copy = this.convert(examine);
        return this.http.post<Examine>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(examine: Examine): Observable<EntityResponseType> {
        const copy = this.convert(examine);
        return this.http.put<Examine>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Examine>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Examine[]>> {
        const options = createRequestOption(req);
        return this.http.get<Examine[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Examine[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Examine = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Examine[]>): HttpResponse<Examine[]> {
        const jsonResponse: Examine[] = res.body;
        const body: Examine[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Examine.
     */
    private convertItemFromServer(examine: Examine): Examine {
        const copy: Examine = Object.assign({}, examine);
        return copy;
    }

    /**
     * Convert a Examine to a JSON which can be sent to the server.
     */
    private convert(examine: Examine): Examine {
        const copy: Examine = Object.assign({}, examine);
        return copy;
    }
}
