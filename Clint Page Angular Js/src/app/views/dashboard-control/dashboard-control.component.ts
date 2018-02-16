import { Component, OnInit } from '@angular/core';
import { ServHttpService } from '../../service/serv-http.service';
import'rxjs/add/operator/map';
@Component({
  selector: 'app-dashboard-control',
  templateUrl: './dashboard-control.component.html',
  styleUrls: ['./dashboard-control.component.css']
})
export class DashboardControlComponent implements OnInit {
   
  message=[
    {title:'Delete All Timer',msg:'Are You Ok For Delete All Timer'},
    {title:'Delete Timer',msg:'Are You Ok For Delete Timer'},
    {title:'Refrech All Timer',msg:'Are You Ok For Refrech Timer'}
  ];

  mesage:any="";
  constructor(public Serv:ServHttpService) { 
    this.Serv.setActiveMenu(2);
  }
  

      
  ngOnInit() {
    document.getElementById("dtp_0aGSs").style.paddingLeft=((window.innerWidth-200)/2)+"px";
   
  }

  clickLed(id,led){
    if( this.Serv.Led[id].state=='on')
      this.Serv.sendLedMessage("led","ledof"+(led));
    else if(this.Serv.Led[id].state=='of')
      this.Serv.sendLedMessage("led","ledon"+(led));
  }


//----------------time-------------------
  Htime:number=0;
  Mtime:number=1;
  StateTimr:string='on';
  id:number;
  setSettingTime(id){
    this.Htime=0;
    this.Mtime=1;
    this.StateTimr='on';
    this.id=id;
    this.Serv.fadein(document.getElementById("timedilog"));  
  }
  incH(){
   this.Htime+=1;
  }
  incM(){
   this.Mtime+=1;
  }
  desH(){
    if(this.Htime>0)
    this.Htime-=1;
    
  }
  desM(){
   if(this.Mtime>1)
     this.Mtime-=1;
  }
  chickOn(){
    this.StateTimr='on';
  }
  chickOf(){
    this.StateTimr='of';
  }

  MessageTitle:string;
  MessageBody:string;
  Msg_Id:number;
  messageBoxShow(msg_id,id=0){
    this.MessageTitle="";
    this.MessageBody="";
    this.Msg_Id=msg_id;
    this.id=0;
    let MSG=document.getElementById('myModalTimers');

    switch(this.Msg_Id){
      case 0://delete all
      this.MessageTitle=this.message[0].title;
      this.MessageBody=this.message[0].msg;
      this.Serv.fadein(MSG);
      break;
      case 1: //delete this
      this.MessageTitle=this.message[1].title;
      this.MessageBody=this.message[1].msg;
      this.id=id;
      this.Serv.fadein(MSG);
      break;
      case 2://refresh
      this.MessageTitle=this.message[2].title;
      this.MessageBody=this.message[2].msg;
      this.id=id;
      this.Serv.fadein(MSG);
      break;
    }
  }
  messageBoxShowOk(){
    switch(this.Msg_Id){
      case 0://delete all
      this.deleteAllTimeLed();
      break;
      case 1: //delete this
      this.deleteTimeLed();
      break;
      case 2://refresh
      this.clickRefrechTimeLed();
      break;
    }
  }
  clickTimeLed(){
    let total:number=( this.Htime * 60) + this.Mtime;
    this.Serv.Led[this.id].refinterval=total+'';
    this.Serv.sendTimeLedMessage("timeled",this.id+'',total, this.StateTimr,"on");
    this.Serv.fadeout(document.getElementById("timedilog"));  
  }
  deleteAllTimeLed(){
    this.Serv.sendTimeLedMessage("timeled","allid",0, this.StateTimr,"ofall");
    this.canselMessageBox(); 
  }
  deleteTimeLed(){
    this.Serv.sendTimeLedMessage("timeled",(this.id)+'',0, this.StateTimr,"of");
    this.canselMessageBox(); 
  }
  clickRefrechTimeLed(){
    let refinterval:number=parseInt(this.Serv.Led[this.id].refinterval);
    this.Serv.sendTimeLedMessage("timeled",this.id+'',refinterval, this.StateTimr,"on");
    this.canselMessageBox(); 
  }
  canselMessageBox(){
    this.Serv.fadeout(document.getElementById('myModalTimers'));
  }
}
