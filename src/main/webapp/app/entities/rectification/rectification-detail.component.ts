import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRectification } from 'app/shared/model/rectification.model';

@Component({
  selector: 'jhi-rectification-detail',
  templateUrl: './rectification-detail.component.html'
})
export class RectificationDetailComponent implements OnInit {
  rectification: IRectification;

  constructor(private activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ rectification }) => {
      this.rectification = rectification;
    });
  }

  previousState() {
    window.history.back();
  }
}
