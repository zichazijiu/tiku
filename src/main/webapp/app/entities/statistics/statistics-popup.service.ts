import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { Statistics } from './statistics.model';
import { StatisticsService } from './statistics.service';

@Injectable()
export class StatisticsPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private statisticsService: StatisticsService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.statisticsService.find(id)
                    .subscribe((statisticsResponse: HttpResponse<Statistics>) => {
                        const statistics: Statistics = statisticsResponse.body;
                        if (statistics.statisticsDate) {
                            statistics.statisticsDate = {
                                year: statistics.statisticsDate.getFullYear(),
                                month: statistics.statisticsDate.getMonth() + 1,
                                day: statistics.statisticsDate.getDate()
                            };
                        }
                        if (statistics.updateDate) {
                            statistics.updateDate = {
                                year: statistics.updateDate.getFullYear(),
                                month: statistics.updateDate.getMonth() + 1,
                                day: statistics.updateDate.getDate()
                            };
                        }
                        this.ngbModalRef = this.statisticsModalRef(component, statistics);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.statisticsModalRef(component, new Statistics());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    statisticsModalRef(component: Component, statistics: Statistics): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.statistics = statistics;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
