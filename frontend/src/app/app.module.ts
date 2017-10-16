import { BrowserModule } from '@angular/platform-browser'
import { NgModule } from '@angular/core'
import { RouterModule, Routes } from '@angular/router'
import { HttpClientModule } from '@angular/common/http'
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'
import { FormsModule } from '@angular/forms'


import { AppComponent } from './app.component'
import { TopbarComponent } from './topbar/topbar.component'
import { HomeComponent } from './home/home.component'
import { SidebarComponent } from './sidebar/sidebar.component'

import { AuthService } from './auth/auth.service'
import { AuthGuard } from './auth/auth.guard'
import { ConfigurationComponent } from './configuration/configuration.component'
import { DisplaValueComponent } from "./configuration/basic/display-value/display-value.component"
import { TwitterFollowersComponent } from "./configuration/twitter/twitter-followers/twitter-followers.component"

const appRoutes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'configuration', component: ConfigurationComponent, canActivate: [AuthGuard],
    children: [
        {
          path: 'display-value',
          component: DisplaValueComponent
        },
        {
          path: 'twitter-followers',
          component: TwitterFollowersComponent
        }
    ]},
  { path: '',
    redirectTo: '/home',
    pathMatch: 'full'
  },
];

@NgModule({
  declarations: [
    AppComponent,
    TopbarComponent,
    HomeComponent,
    SidebarComponent,
    ConfigurationComponent,
    DisplaValueComponent,
    TwitterFollowersComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    RouterModule.forRoot(
      appRoutes    
    )
  ],
  providers: [
    AuthService,
    AuthGuard
  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule { }
