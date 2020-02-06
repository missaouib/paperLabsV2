import { Component, Input, OnInit } from '@angular/core';
import { BlocEntity } from 'app/entities/BlocEntity';

@Component({
  selector: 'jhi-bloc',
  templateUrl: './bloc.component.html',
  styleUrls: ['bloc.scss']
})
export class BlocComponent implements OnInit {
  @Input() bloc: BlocEntity;

  constructor() {}

  ngOnInit() {}
}
