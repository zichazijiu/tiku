import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Report } from './report.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Report>;

@Injectable()
export class ReportService {

    private resourceUrl =  SERVER_API_URL + 'api/reports';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(report: Report): Observable<EntityResponseType> {
        const copy = this.convert(report);
        return this.http.post<Report>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(report: Report): Observable<EntityResponseType> {
        const copy = this.convert(report);
        return this.http.put<Report>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Report>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Report[]>> {
        const options = createRequestOption(req);
        return this.http.get<Report[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Report[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Report = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Report[]>): HttpResponse<Report[]> {
        const jsonResponse: Report[] = res.body;
        const body: Report[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Report.
     */
    private convertItemFromServer(report: Report): Report {
        const copy: Report = Object.assign({}, report);
        copy.createdTime = this.dateUtils
            .convertDateTimeFromServer(report.createdTime);
        copy.reportTime = this.dateUtils
            .convertDateTimeFromServer(report.reportTime);
        return copy;
    }

    /**
     * Convert a Report to a JSON which can be sent to the server.
     */
    private convert(report: Report): Report {
        const copy: Report = Object.assign({}, report);

        copy.createdTime = this.dateUtils.toDate(report.createdTime);

        copy.reportTime = this.dateUtils.toDate(report.reportTime);
        return copy;
    }
}
