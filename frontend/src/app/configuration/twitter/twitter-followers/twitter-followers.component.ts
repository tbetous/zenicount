import { Component, OnInit } from '@angular/core'
import { HttpClient } from '@angular/common/http'

@Component({
  selector: 'configuration-twitter-followers',
  templateUrl: './twitter-followers.html',
  styleUrls: ['./twitter-followers.css']
})
export class TwitterFollowersComponent implements OnInit {

  public stringId

  constructor(private http : HttpClient) {}

  ngOnInit() {
  }

  loadConfiguration() {
    this.http.post('/api/subscribe/twitterFollowers', {stringId: this.stringId}).subscribe(() => {
      this.stringId = ""
    })
  }

}