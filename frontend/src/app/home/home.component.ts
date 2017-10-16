import { Component, OnInit } from '@angular/core';
import { Observable } from "rxjs"
import { trigger,state,style,transition,animate,keyframes } from '@angular/animations';
import { AuthService } from '../auth/auth.service'

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  animations: [
    trigger('moveLetter', [
      state('middle', style({
            top: '0px',
        })),
        state('top', style({
            top: '-200px',
        })),
        state('bottom', style({
            top: '200px',
        })),
        transition('middle => top', animate('500ms ease-in',  keyframes([
          style({top: '0px',  offset: 0}),
          style({top: '10px', offset: 0.3}),
          style({top: '-200px', offset: 1})
        ]))),
        transition('top => middle', animate('500ms ease-in', keyframes([
          style({top: '200px', offset: 0}),
          style({top: '-10px', offset: 0.7}),
          style({top: '0px',  offset: 1})
        ]))),
    ])
  ]
})
export class HomeComponent implements OnInit {
  public states = new Array(9).fill("middle")
  public isAuthenticated = false

  constructor(private authService : AuthService) { }

  ngOnInit() {
    this.states.forEach((e,i) => this.startLetterMoveProcess(i))
    this.authService.isAuthenticated().subscribe(({value} : any) => {
      this.isAuthenticated = value
    })
  }

  pickTime() {
    return Math.floor((Math.random() * 7500) + 1000)
  }

  onLetterTop(index) {
    if(this.states[index] === 'top') {
      this.states[index] = 'middle'
      this.startLetterMoveProcess(index)
    }
  }

  startLetterMoveProcess(index) {
    Observable.interval(this.pickTime()).first().subscribe(() => {
      this.states[index] = 'top'
    })
  }
}
