import { Component, OnInit } from '@angular/core';
import { ServHttpService } from '../service/serv-http.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {


  constructor(public Serv:ServHttpService ,private router:Router) { }
  
  AllLed=[{title:'On All Timer',msg:'Are You Ok For On All Timer',state:'myModalon'},
  {title:'Off All Timer',msg:'Are You Ok For Off All Timer',state:'myModaloff'},
  {title:'PIR Sensor On',msg:'Are You Ok For On Sensor Pir',state:'myModal_on_pir'},
  {title:'PIR Sensor Off',msg:'Are You Ok For Off Sensor Pir',state:'myModal_off_pir'},
  {title:'IR Sensor On',msg:'Are You Ok For On Sensor Ir',state:'myModal_on_ir'},
  {title:'IR Sensor Off',msg:'Are You Ok For Off Sensor Ir',state:'myModal_off_ir'}];

  clickRunAllLed(state){
    if(state=="myModalon")
      this.Serv.sendLedMessage("allled",'ledallrun'); //ledallrun
    else if(state=="myModaloff")
    this.Serv.sendLedMessage("allled","ledallstop"); //ledallstop
    else if(state=="myModal_on_ir")
      this.Serv.sendSensorLedMessage("sensor","ir","on");
    else if(state=="myModal_off_ir")
    this.Serv.sendSensorLedMessage("sensor","ir","of");
    else if(state=="myModal_on_pir")
    this.Serv.sendSensorLedMessage("sensor","pir","on");
    else if(state=="myModal_off_pir")
    this.Serv.sendSensorLedMessage("sensor","pir","of");
  
  }
  ngOnInit(): void {
    this.Serv.getSocket().emit("clintuserlogin",'{"email":"'+this.Serv.UserName.User+'"'+
    ',"password":"'+this.Serv.UserName.Pass+'"}');
    this.ChechUserAdmin();
    this.Serv.getSocket().on("message",(data)=>{
      let command=JSON.parse(data.msg);

      if( command.msg=="ledof" )
      this.Serv.Led[command.numb-ServHttpService.Lenght].state='of';

    else if( command.msg=="ledon" )
        this.Serv.Led[command.numb-ServHttpService.Lenght].state='on';

    else if(command.msg=="timeled"){
      if(command.STL=="on"||command.STL=="of"){
        this.Serv.Led[command.id].ledinterval=command.ledinterval;
        this.Serv.Led[command.id].statetime=command.statetime;
        this.Serv.Led[command.id].STL=command.STL;      
      }else  if(command.STL=="ofall"){
        this.Serv.Led.forEach(element => {
          element.STL="of";//comment
        });
      }
    }

    
    else if(command.msg =='ledallrun')
      this.Serv.Led.forEach(element => {
        element.state='on';
      });
     else if(command.msg=='ledallstop')
      this.Serv.Led.forEach(element => {
        element.state='of';
      });
      //---------------
      else if(command.msg=="lediron")
       this.Serv.Sensor_State.SIR=true;
      else if(command.msg=="ledirstop")
       this.Serv.Sensor_State.SIR=false;
       //--
       if(command.msg=="ledpiron")
       this.Serv.Sensor_State.SPIR=true;
      else if(command.msg=="ledpirstop")
       this.Serv.Sensor_State.SPIR=false;

    
        else if (command.msg=="kik"){
          if(command.name==this.Serv.UserName.User)
          window.location.href="http://"+ServHttpService.ip+":3000";
           
        }

    });
  }

  ChechUserAdmin(){
    if(this.Serv.UserName.State!="admin"){
      document.getElementById("msterdash").remove();
      document.getElementById("mangeuser").remove();
      document.getElementById("settuser").remove(); 
      this.router.navigateByUrl('/dash');
      this.router.ngOnDestroy();
    }
  }

  logOut(){
    window.location.href="http://"+ServHttpService.ip+":3000/logout";
  }

}
