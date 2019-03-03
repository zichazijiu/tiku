import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Menu } from './menu.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Menu>;

@Injectable()
export class MenuService {

    private resourceUrl =  SERVER_API_URL + 'api/menus';

    constructor(private http: HttpClient) { }

    create(menu: Menu): Observable<EntityResponseType> {
        const copy = this.convert(menu);
        return this.http.post<Menu>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(menu: Menu): Observable<EntityResponseType> {
        const copy = this.convert(menu);
        return this.http.put<Menu>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Menu>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Menu[]>> {
        const options = createRequestOption(req);
        return this.http.get<Menu[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Menu[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Menu = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Menu[]>): HttpResponse<Menu[]> {
        const jsonResponse: Menu[] = res.body;
        const body: Menu[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Menu.
     */
    private convertItemFromServer(menu: Menu): Menu {
        const copy: Menu = Object.assign({}, menu);
        return copy;
    }

    /**
     * Convert a Menu to a JSON which can be sent to the server.
     */
    private convert(menu: Menu): Menu {
        const copy: Menu = Object.assign({}, menu);
        return copy;
    }
}
