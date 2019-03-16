import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { RemainsQuestion } from './remains-question.model';
import { RemainsQuestionService } from './remains-question.service';

@Injectable()
export class RemainsQuestionPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private remainsQuestionService: RemainsQuestionService

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
                this.remainsQuestionService.find(id)
                    .subscribe((remainsQuestionResponse: HttpResponse<RemainsQuestion>) => {
                        const remainsQuestion: RemainsQuestion = remainsQuestionResponse.body;
                        remainsQuestion.createdTime = this.datePipe
                            .transform(remainsQuestion.createdTime, 'yyyy-MM-ddTHH:mm:ss');
                        this.ngbModalRef = this.remainsQuestionModalRef(component, remainsQuestion);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.remainsQuestionModalRef(component, new RemainsQuestion());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    remainsQuestionModalRef(component: Component, remainsQuestion: RemainsQuestion): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.remainsQuestion = remainsQuestion;
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
