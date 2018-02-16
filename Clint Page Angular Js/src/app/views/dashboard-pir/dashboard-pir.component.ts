import { Component, OnInit } from '@angular/core';
import { ServHttpService } from '../../service/serv-http.service';

@Component({
  selector: 'app-dashboard-pir',
  templateUrl: './dashboard-pir.component.html',
  styleUrls: ['./dashboard-pir.component.css']
})
export class DashboardPirComponent implements OnInit {
 PROGRASS_DOM:any=null;
 PROGRASSSend_DOM:any=null;
  Led=[{mode:1,name:"ON--->OFF",state:true,click:false},
  {mode:2,name:"ON",state:true,click:true},
  {mode:3,name:"OFF",state:false,click:false},
  {mode:4,name:"OFF--->ON",state:false,click:false}];

  constructor(public Serv:ServHttpService) {     this.Serv.setActiveMenu(3);}

  ngOnInit() {
  this.PROGRASS_DOM     =document.getElementById("pir_prog");
  this.PROGRASSSend_DOM =document.getElementById("progloadeid");
  this.PROGRASS_DOM.style.width=this.Serv.Sensor_State.time+"%";
   this.setEfectPir();
   this.Serv.getSocket().on("message",(data)=>{
    let command=JSON.parse(data.msg);
    if( command.msg=="option_pir" ){
      this.Serv.Sensor_State.time=parseInt(command.time);
      this.PROGRASS_DOM.style.width=this.Serv.Sensor_State.time+"%";
      this.clickLedOption(command.mode);
      let new_led = JSON.parse(command.led);
      new_led.forEach((element,index) => {
        this.Serv.Led[index].pir=element;
      });
    }
   });
  }

  setEfectPir(){
    setInterval(()=>{
      this.Led.forEach(element => {
        switch (element.name) {
          case "ON--->OFF": this.Led[0].state= !(element.state); break;
          case "OFF--->ON": this.Led[3].state= !(element.state); break;
        } 
      });
    },1000);
  }
  clickLedOption(mode){
    this.Serv.Sensor_State.mode=mode;
    this.Led.forEach(element => {
      if(element.mode==mode)
        element.click=true;
      else element.click=false;
    });
  }

  pirPlusTime(){
    if(this.Serv.Sensor_State.time<100){
      this.Serv.Sensor_State.time++;
      this.PROGRASS_DOM.style.width=this.Serv.Sensor_State.time+"%";
    }
  }
  pirMinceTime(){
    if(this.Serv.Sensor_State.time >5){
      this.Serv.Sensor_State.time--;
      this.PROGRASS_DOM.style.width=this.Serv.Sensor_State.time+"%";
    }
  }
  sendDataPir(){//--
     let led=new Array();
     for(let i=0; i< this.Serv.Led.length ;i++){
      led.push(this.Serv.Led[i].pir);
     }
    this.Serv.sendSensorPirMessage("option_pir",this.Serv.Sensor_State.time,this.Serv.Sensor_State.mode,'['+led+']');
    this.Serv. sendDataEffect(this.PROGRASSSend_DOM);
  }
  addPirLed(led_id){
    if(this.Serv.Led[led_id].pir)
    this.Serv.Led[led_id].pir=0;//false
    else
      this.Serv.Led[led_id].pir=1;//true

  }



}

