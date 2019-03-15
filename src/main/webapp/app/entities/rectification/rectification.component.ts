import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IRectification } from 'app/shared/model/rectification.model';
import { Principal } from 'app/core';
import { RectificationService } from './rectification.service';

@Component({
  selector: 'jhi-rectification',
  templateUrl: './rectification.component.html'
})
export class RectificationComponent implements OnInit, OnDestroy {
  rectifications: IRectification[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    private rectificationService: RectificationService,
    private jhiAlertService: JhiAlertService,
    private eventManager: JhiEventManager,
    private principal: Principal
  ) {}

  loadAll() {
    this.rectificationService.query().subscribe(
      (res: HttpResponse<IRectification[]>) => {
        this.rectifications = res.body;
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  ngOnInit() {
    this.loadAll();
    this.principal.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInRectifications();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IRectification) {
    return item.id;
  }

  registerChangeInRectifications() {
    this.eventSubscriber = this.eventManager.subscribe('rectificationListModification', response => this.loadAll());
  }

  private onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
