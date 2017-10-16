import { Resolve } from '@angular/router';
import { Observable } from 'rxjs'
import { AuthService } from './auth.service'

export class AuthResolver implements Resolve<Boolean> {

    constructor(private authService : AuthService){}

    resolve(): Observable<Boolean> {
        return this.authService.isAuthenticated()
    }
}