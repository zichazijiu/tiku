import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { CheckItemAnswer } from './check-item-answer.model';
import { CheckItemAnswerService } from './check-item-answer.service';

@Injectable()
export class CheckItemAnswerPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private checkItemAnswerService: CheckItemAnswerService

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
                this.checkItemAnswerService.find(id)
                    .subscribe((checkItemAnswerResponse: HttpResponse<CheckItemAnswer>) => {
                        const checkItemAnswer: CheckItemAnswer = checkItemAnswerResponse.body;
                        this.ngbModalRef = this.checkItemAnswerModalRef(component, checkItemAnswer);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.checkItemAnswerModalRef(component, new CheckItemAnswer());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    checkItemAnswerModalRef(component: Component, checkItemAnswer: CheckItemAnswer): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.checkItemAnswer = checkItemAnswer;
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
