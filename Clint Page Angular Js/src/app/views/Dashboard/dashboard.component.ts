import { Component, OnInit } from '@angular/core';
import { ServHttpService } from '../../service/serv-http.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  PROGRASSSend_DOM: HTMLElement;
  last_Users;
  active_Users;
  info_dash={cont:{member:0,PandingMember:0},member:0,req:"0"};
  constructor(public Serv:ServHttpService) { 
    this.Serv.setActiveMenu(1);
  }

  ngOnInit() {
    this.PROGRASSSend_DOM =document.getElementById("progloadeid");
    this.getInfoDash();
    this.getInfoDashUser();
    this.getInfoActiveUsers(); 
    this.Serv.getSocket().on("message",(data)=>{ 
       this.info_dash.req+=1;
       let command=JSON.parse(data.msg);
       if( command.msg=="kik" ){
          this.Serv.fadeout(document.getElementById("kik"+command.name));
          this.info_dash.member-=1;
        }
    });
  }

  getInfoDash(){
     this.Serv.sendDataEffect(this.PROGRASSSend_DOM);
     this.Serv.servPost('http://'+ServHttpService.ip+':3000/getdash' ,{email:this.Serv.UserName.User ,password:this.Serv.UserName.Pass}).subscribe((data) => {
      if(data.state){
        this.info_dash= data;
      }else{
        this.Serv.goLocation('login');
      }

      });   
  }
  getInfoDashUser(){
    this.Serv.sendDataEffect(this.PROGRASSSend_DOM);
    this.Serv.servPost('http://'+ServHttpService.ip+':3000/get10user' ,{email:this.Serv.UserName.User ,password:this.Serv.UserName.Pass}).subscribe((data) => {
    if(data.state){
      this.last_Users=data.users;
    }else{
      this.Serv.goLocation('login');
    }
    });   
  }
  getInfoActiveUsers(){
    this.Serv.sendDataEffect(this.PROGRASSSend_DOM);
    this.Serv.servPost('http://'+ServHttpService.ip+':3000/getactiveuser' ,{email:this.Serv.UserName.User ,password:this.Serv.UserName.Pass}).subscribe((data) => {
    if(data.state){
      this.active_Users=data.users;
    }else{
      this.Serv.goLocation('login');
    }
    });   
  }

  kikOutUser(id,name){
    this.Serv.sendKikoutUser("kik",id,name);
  }

}
