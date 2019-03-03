import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Menu } from './menu.model';
import { MenuService } from './menu.service';

@Component({
    selector: 'jhi-menu-detail',
    templateUrl: './menu-detail.component.html'
})
export class MenuDetailComponent implements OnInit, OnDestroy {

    menu: Menu;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private menuService: MenuService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMenus();
    }

    load(id) {
        this.menuService.find(id)
            .subscribe((menuResponse: HttpResponse<Menu>) => {
                this.menu = menuResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMenus() {
        this.eventSubscriber = this.eventManager.subscribe(
            'menuListModification',
            (response) => this.load(this.menu.id)
        );
    }
}
