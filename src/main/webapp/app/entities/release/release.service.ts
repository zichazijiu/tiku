import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Release } from './release.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Release>;

@Injectable()
export class ReleaseService {

    private resourceUrl =  SERVER_API_URL + 'api/releases';

    constructor(private http: HttpClient) { }

    create(release: Release): Observable<EntityResponseType> {
        const copy = this.convert(release);
        return this.http.post<Release>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(release: Release): Observable<EntityResponseType> {
        const copy = this.convert(release);
        return this.http.put<Release>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Release>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Release[]>> {
        const options = createRequestOption(req);
        return this.http.get<Release[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Release[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Release = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Release[]>): HttpResponse<Release[]> {
        const jsonResponse: Release[] = res.body;
        const body: Release[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Release.
     */
    private convertItemFromServer(release: Release): Release {
        const copy: Release = Object.assign({}, release);
        return copy;
    }

    /**
     * Convert a Release to a JSON which can be sent to the server.
     */
    private convert(release: Release): Release {
        const copy: Release = Object.assign({}, release);
        return copy;
    }
}
