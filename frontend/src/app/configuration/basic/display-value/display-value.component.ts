import { Component, OnInit } from '@angular/core'
import { HttpClient } from '@angular/common/http'

@Component({
  selector: 'configuration-display-value',
  templateUrl: './display-value.html',
  styleUrls: ['./display-value.css']
})
export class DisplaValueComponent implements OnInit {

  public count

  constructor(private http : HttpClient) {}

  ngOnInit() {
  }

  loadConfiguration() {
    this.http.post('/api/subscribe/basic', {start: this.count}).subscribe(() => {
      this.count = undefined
    })
  }

}