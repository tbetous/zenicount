import { Injectable } from '@angular/core'
import { HttpClient } from '@angular/common/http'
import { BehaviorSubject, Observable } from 'rxjs'
import { Router } from '@angular/router'

@Injectable()
export class AuthService {

  private authenticated = new BehaviorSubject<boolean>(false)

  constructor(private http : HttpClient, private router : Router) { }

  checkAuthentication() {
    this.http.get<{value: boolean}>('/api/user/authenticated')
      .first()
      .subscribe(({value}) => this.authenticated.next(value))
  }

  isAuthenticated() {
    return this.authenticated
  }

  logout() {
    this.http.get('/api/logout').subscribe(() => {
      this.authenticated.next(false)
      this.router.navigate(['/'])
    })
  }

}
