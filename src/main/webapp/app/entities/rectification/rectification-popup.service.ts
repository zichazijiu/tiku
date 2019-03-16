import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { Rectification } from './rectification.model';
import { RectificationService } from './rectification.service';

@Injectable()
export class RectificationPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private rectificationService: RectificationService

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
                this.rectificationService.find(id)
                    .subscribe((rectificationResponse: HttpResponse<Rectification>) => {
                        const rectification: Rectification = rectificationResponse.body;
                        rectification.rectificationTime = this.datePipe
                            .transform(rectification.rectificationTime, 'yyyy-MM-ddTHH:mm:ss');
                        this.ngbModalRef = this.rectificationModalRef(component, rectification);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.rectificationModalRef(component, new Rectification());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    rectificationModalRef(component: Component, rectification: Rectification): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.rectification = rectification;
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
