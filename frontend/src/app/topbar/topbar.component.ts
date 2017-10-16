import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth/auth.service'

@Component({
  selector: 'topbar',
  templateUrl: './topbar.component.html',
  styleUrls: ['./topbar.component.css']
})
export class TopbarComponent implements OnInit {
  public isAuthenticated

  constructor(private authService: AuthService) { }

  ngOnInit() {
    this.isAuthenticated = this.authService.isAuthenticated()
    this.authService.checkAuthentication()
  }

  logout() {
    this.authService.logout()
  }

}
