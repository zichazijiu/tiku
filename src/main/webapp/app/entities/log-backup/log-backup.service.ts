import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { LogBackup } from './log-backup.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<LogBackup>;

@Injectable()
export class LogBackupService {

    private resourceUrl =  SERVER_API_URL + 'api/log-backups';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(logBackup: LogBackup): Observable<EntityResponseType> {
        const copy = this.convert(logBackup);
        return this.http.post<LogBackup>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(logBackup: LogBackup): Observable<EntityResponseType> {
        const copy = this.convert(logBackup);
        return this.http.put<LogBackup>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<LogBackup>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<LogBackup[]>> {
        const options = createRequestOption(req);
        return this.http.get<LogBackup[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<LogBackup[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: LogBackup = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<LogBackup[]>): HttpResponse<LogBackup[]> {
        const jsonResponse: LogBackup[] = res.body;
        const body: LogBackup[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to LogBackup.
     */
    private convertItemFromServer(logBackup: LogBackup): LogBackup {
        const copy: LogBackup = Object.assign({}, logBackup);
        copy.createdTime = this.dateUtils
            .convertDateTimeFromServer(logBackup.createdTime);
        return copy;
    }

    /**
     * Convert a LogBackup to a JSON which can be sent to the server.
     */
    private convert(logBackup: LogBackup): LogBackup {
        const copy: LogBackup = Object.assign({}, logBackup);

        copy.createdTime = this.dateUtils.toDate(logBackup.createdTime);
        return copy;
    }
}
