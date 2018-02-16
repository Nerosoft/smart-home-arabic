import { Injectable } from '@angular/core';
import {  Http , Headers} from '@angular/http';
import {  HttpClient } from '@angular/common/http';
import { Socket } from 'ng-socket-io';

import { Observable } from 'rxjs/Observable';
import'rxjs/add/operator/map';

@Injectable()
export class ServHttpService {
  PROGRASSSend_DOM: HTMLElement;
  static Lenght=4;
  UserName={User:"",Pass:"",State:""};
  static ip:string="127.0.0.1";
  constructor( private http:Http,private httpp:HttpClient,private socket: Socket) {
    this.PROGRASSSend_DOM =document.getElementById("progloadeid");

    let dom_User:HTMLElement = document.getElementById("User");
    let dom_Pass:HTMLElement = document.getElementById("Pass");
    let dom_state:HTMLElement = document.getElementById("state");

    this.UserName.User=dom_User.innerText;
    this.UserName.Pass=dom_Pass.innerText;
    this.UserName.State=dom_state.innerText;
    
    dom_User.remove();
    dom_Pass.remove();
    dom_state.remove();
    this.getSettingLedInfo();
  }
  Sensor_State={id:0 ,SPIR:false ,time:5,mode:"2",key:1 ,SIR:false};
  menu={master_dash:true,led_dash:false,pir:false,cange_user:false,add_user:false,edite_user:false,edite_name_led:false};
  Led=[];

 

 getUrl(){return this.http;}
  servPost(url:string,user){
    let headers=new Headers();
    headers.append('Content-type','application/json');
    return  this.http
    .post(url,user,{headers:headers}).map(res=>res.json());
  }

  servGet(url:string){
    let headers=new Headers();
    headers.append('Content-type','application/json');
    return  this.http
    .get(url,{headers:headers}).map(res=>res.json());
  }
  
  sendLedMessage(msg: string,led:string){
    this.socket.emit("clintuser",'{"email":"'+this.UserName.User+'","password":"'+this.UserName.Pass+'","typemessage":"'+msg+'","led":"'+led+':"}' );
  }

  sendTimeLedMessage(msg: string,id:string,ledinterval:number,statetime:string,STL:string){
    this.socket.emit("clintuser",'{"email":"'+this.UserName.User+'","password":"'+this.UserName.Pass+'","typemessage":"'+msg+'" ,"id":"'+id+'" ,"ledinterval":"'+ledinterval+'" ,"refinterval":"'+ledinterval+'" ,"statetime":"'+statetime+'","STL":"'+STL+'"}' );
  }

  sendSensorLedMessage(msg: string,sensor:string,state:string){
    this.socket.emit("clintuser",'{"email":"'+this.UserName.User+'","password":"'+this.UserName.Pass+'","typemessage":"'+msg+'" ,"sensor":"'+sensor+'","state":"'+state+'"}' );
  }


  sendSensorPirMessage(msg: string,time:number,mode:string,led:String,state:string="send"){
    this.socket.emit("clintuser",'{"email":"'+this.UserName.User+'","password":"'+this.UserName.Pass+'","typemessage":"'+msg+'" ,"time":"'+time+'","mode":"'+mode+'","led":"'+led+'","state":"'+state+'"}' );
  }

    sendKikoutUser(msg: string ,id:string ,name:String){
      this.socket.emit("clintuser",'{"email":"'+this.UserName.User+'","password":"'+this.UserName.Pass+'","typemessage":"'+msg+'" ,"id":"'+id+'","name":"'+name+'"}' );
    }

  getSocket(){
    return this.socket;
  }


  fadeout(element) {
    let op = 1;  // initial opacity
    let timer = setInterval(function () {
        if (op <= 0.1){
            clearInterval(timer);
            element.style.display = 'none';
        }
        element.style.opacity = op;
        element.style.filter = 'alpha(opacity=' + op * 100 + ")";
        op -= op * 0.3;
    }, 25);
  }
  
  fadein(element) {
    let op = 0.1;  // initial opacity
    let timer = setInterval(function () {
      if(op==0.1)
           element.style.display = 'block';
      if (op >= 0.9)
           clearInterval(timer);
        
        element.style.opacity = op;
        element.style.filter = 'alpha(opacity=' + op * 100 + ")";
        op += op * 0.1;
    }, 7);
  }
  sendDataEffect(dom){
    let loadeprog=5;
    dom.parentElement.style.display = 'block';
    let timer= setInterval(function(){
      loadeprog+=7;
      dom.style.width=loadeprog+"%";
      if(loadeprog>=120){
       clearInterval(timer);
       dom.parentElement.style.display = 'none';
       dom.style.width=0+"%";
       loadeprog=5
      }
    },100);
  }

  getSettingLedInfo(){
    this.servPost('http://'+ServHttpService.ip+':3000/settingLed',{email:this.UserName.User ,password:this.UserName.Pass}).subscribe((data) => {
        if(data.state){
          this.Led= data.Led;
          this.Sensor_State=data.Setting[0];
        }
        else this.goLocation('login');
      });   
  }

  goLocation(location){
    switch (location) {
      case 'login':window.location.href="http://"+ServHttpService.ip+":3000";break;
    
      default:
        break;
    }
  }

  
  setActiveMenu(id){
    
        switch (id) {
          case 1: 
          this.menu.master_dash=true;
          this.menu.led_dash=false;
          this.menu.pir=false;
          this.menu.cange_user=false;
          this.menu.add_user=false;
          this.menu.edite_user=false;
          this.menu.edite_name_led=false;
          break;
          case 2: 
          this.menu.master_dash=false;
          this.menu.led_dash=true;
          this.menu.pir=false;
          this.menu.cange_user=false;
          this.menu.add_user=false;
          this.menu.edite_user=false;
          this.menu.edite_name_led=false;
          break;
          case 3: 
          this.menu.master_dash=false;
          this.menu.led_dash=false;
          this.menu.pir=true;
          this.menu.cange_user=false;
          this.menu.add_user=false;
          this.menu.edite_user=false;
          this.menu.edite_name_led=false;
          break;
          case 4: 
          this.menu.master_dash=false;
          this.menu.led_dash=false;
          this.menu.pir=false;
          this.menu.cange_user=true;
          this.menu.add_user=false;
          this.menu.edite_user=false;
          this.menu.edite_name_led=false;
          break;
          case 5: 
          this.menu.master_dash=false;
          this.menu.led_dash=false;
          this.menu.pir=false;
          this.menu.cange_user=false;
          this.menu.add_user=true;
          this.menu.edite_user=false;
          this.menu.edite_name_led=false;
          break;
          case 6: 
          this.menu.master_dash=false;
          this.menu.led_dash=false;
          this.menu.pir=false;
          this.menu.cange_user=false;
          this.menu.add_user=false;
          this.menu.edite_user=true;
          this.menu.edite_name_led=false;
          break;
          case 7: 
          this.menu.master_dash=false;
          this.menu.led_dash=false;
          this.menu.pir=false;
          this.menu.cange_user=false;
          this.menu.add_user=false;
          this.menu.edite_user=false;
          this.menu.edite_name_led=true;
          break;
          default: break;
        }
      }

}

