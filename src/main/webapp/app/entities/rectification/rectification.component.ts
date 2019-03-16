import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Rectification } from './rectification.model';
import { RectificationService } from './rectification.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-rectification',
    templateUrl: './rectification.component.html'
})
export class RectificationComponent implements OnInit, OnDestroy {
rectifications: Rectification[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private rectificationService: RectificationService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.rectificationService.query().subscribe(
            (res: HttpResponse<Rectification[]>) => {
                this.rectifications = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInRectifications();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Rectification) {
        return item.id;
    }
    registerChangeInRectifications() {
        this.eventSubscriber = this.eventManager.subscribe('rectificationListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
