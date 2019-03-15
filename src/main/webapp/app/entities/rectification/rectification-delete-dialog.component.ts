import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRectification } from 'app/shared/model/rectification.model';
import { RectificationService } from './rectification.service';

@Component({
  selector: 'jhi-rectification-delete-dialog',
  templateUrl: './rectification-delete-dialog.component.html'
})
export class RectificationDeleteDialogComponent {
  rectification: IRectification;

  constructor(
    private rectificationService: RectificationService,
    public activeModal: NgbActiveModal,
    private eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.rectificationService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'rectificationListModification',
        content: 'Deleted an rectification'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-rectification-delete-popup',
  template: ''
})
export class RectificationDeletePopupComponent implements OnInit, OnDestroy {
  private ngbModalRef: NgbModalRef;

  constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ rectification }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(RectificationDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.rectification = rectification;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
