import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-employers',
  templateUrl: './employers.component.html',
  styleUrls: ['./employers.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class EmployersComponent implements OnInit {
  navLinks: any[];
  activeLinkIndex = -1;

  constructor(private router: Router) {
    this.navLinks = [
      {
        label: 'Sociétés',
        link: './company',
        index: 0
      },
      {
        label: 'Particuliers',
        link: './employer',
        index: 1
      }
    ];
  }

  ngOnInit() {
    this.router.events.subscribe(res => {
      this.activeLinkIndex = this.navLinks.indexOf(this.navLinks.find(tab => tab.link === '.' + this.router.url));
    });
  }
}
