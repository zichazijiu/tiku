import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRemainsQuestion } from 'app/shared/model/remains-question.model';
import { RemainsQuestionService } from './remains-question.service';

@Component({
  selector: 'jhi-remains-question-delete-dialog',
  templateUrl: './remains-question-delete-dialog.component.html'
})
export class RemainsQuestionDeleteDialogComponent {
  remainsQuestion: IRemainsQuestion;

  constructor(
    private remainsQuestionService: RemainsQuestionService,
    public activeModal: NgbActiveModal,
    private eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.remainsQuestionService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'remainsQuestionListModification',
        content: 'Deleted an remainsQuestion'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-remains-question-delete-popup',
  template: ''
})
export class RemainsQuestionDeletePopupComponent implements OnInit, OnDestroy {
  private ngbModalRef: NgbModalRef;

  constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ remainsQuestion }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(RemainsQuestionDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.remainsQuestion = remainsQuestion;
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
