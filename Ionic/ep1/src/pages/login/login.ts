import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';

import { HelloIonicPage } from '../hello-ionic/hello-ionic'
import { User } from '../../models/user' 
import { Seminar } from '../../models/seminar' 


/**
 * Generated class for the Login page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
@Component({
  selector: 'page-login',
  templateUrl: 'login.html',
})
export class Login {
  public user: User;
  info = {}

  constructor(public navCtrl: NavController, public navParams: NavParams) {
    this.user = new User("123", "EU", true);
    console.log(this.user.nusp);
  }

  logForm() {
    console.log(this.info)
    // POST login
    this.navCtrl.push(HelloIonicPage);
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad Login');
  }

}
