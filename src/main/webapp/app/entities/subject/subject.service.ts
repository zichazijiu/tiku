import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Subject } from './subject.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Subject>;

@Injectable()
export class SubjectService {

    private resourceUrl =  SERVER_API_URL + 'api/subjects';

    constructor(private http: HttpClient) { }

    create(subject: Subject): Observable<EntityResponseType> {
        const copy = this.convert(subject);
        return this.http.post<Subject>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(subject: Subject): Observable<EntityResponseType> {
        const copy = this.convert(subject);
        return this.http.put<Subject>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Subject>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Subject[]>> {
        const options = createRequestOption(req);
        return this.http.get<Subject[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Subject[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Subject = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Subject[]>): HttpResponse<Subject[]> {
        const jsonResponse: Subject[] = res.body;
        const body: Subject[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Subject.
     */
    private convertItemFromServer(subject: Subject): Subject {
        const copy: Subject = Object.assign({}, subject);
        return copy;
    }

    /**
     * Convert a Subject to a JSON which can be sent to the server.
     */
    private convert(subject: Subject): Subject {
        const copy: Subject = Object.assign({}, subject);
        return copy;
    }
}
