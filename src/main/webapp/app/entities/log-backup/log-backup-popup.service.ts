import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { LogBackup } from './log-backup.model';
import { LogBackupService } from './log-backup.service';

@Injectable()
export class LogBackupPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private logBackupService: LogBackupService

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
                this.logBackupService.find(id)
                    .subscribe((logBackupResponse: HttpResponse<LogBackup>) => {
                        const logBackup: LogBackup = logBackupResponse.body;
                        logBackup.createdTime = this.datePipe
                            .transform(logBackup.createdTime, 'yyyy-MM-ddTHH:mm:ss');
                        this.ngbModalRef = this.logBackupModalRef(component, logBackup);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.logBackupModalRef(component, new LogBackup());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    logBackupModalRef(component: Component, logBackup: LogBackup): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.logBackup = logBackup;
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
