import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { ReleaseHistory } from './release-history.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<ReleaseHistory>;

@Injectable()
export class ReleaseHistoryService {

    private resourceUrl =  SERVER_API_URL + 'api/release-histories';

    constructor(private http: HttpClient) { }

    create(releaseHistory: ReleaseHistory): Observable<EntityResponseType> {
        const copy = this.convert(releaseHistory);
        return this.http.post<ReleaseHistory>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(releaseHistory: ReleaseHistory): Observable<EntityResponseType> {
        const copy = this.convert(releaseHistory);
        return this.http.put<ReleaseHistory>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ReleaseHistory>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<ReleaseHistory[]>> {
        const options = createRequestOption(req);
        return this.http.get<ReleaseHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<ReleaseHistory[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: ReleaseHistory = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<ReleaseHistory[]>): HttpResponse<ReleaseHistory[]> {
        const jsonResponse: ReleaseHistory[] = res.body;
        const body: ReleaseHistory[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to ReleaseHistory.
     */
    private convertItemFromServer(releaseHistory: ReleaseHistory): ReleaseHistory {
        const copy: ReleaseHistory = Object.assign({}, releaseHistory);
        return copy;
    }

    /**
     * Convert a ReleaseHistory to a JSON which can be sent to the server.
     */
    private convert(releaseHistory: ReleaseHistory): ReleaseHistory {
        const copy: ReleaseHistory = Object.assign({}, releaseHistory);
        return copy;
    }
}
