import { Component, OnInit } from '@angular/core';
import { ServHttpService } from '../../service/serv-http.service';

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css']
})
export class EditComponent implements OnInit {
  ID;
  AllLed={title:'',msg:'',state:'myModalchang',id:""};
  constructor(public Serv:ServHttpService){  
    this.Serv.setActiveMenu(7);
    this.Serv.Led.forEach(element => {
      element.newname=element.name
    });
  }

  ngOnInit() {
    
  }

  clickToCange(id){
    this.ID=id;
    this.AllLed.title="Chang Led "+this.Serv.Led[id].name;
    this.AllLed.msg="Do You Want Chang led ( "+this.Serv.Led[id].name+" ) TO "+this.Serv.Led[id].newname;
    this.Serv.fadein(document.getElementById(this.AllLed.state));
  }
  closeMessage(){
    this.Serv.fadeout(document.getElementById(this.AllLed.state));
  }
  clickToChangLed(){      
  
    this.Serv.servPost('http://'+ServHttpService.ip+':3000/updateledname',{id:this.ID ,newname:this.Serv.Led[this.ID].newname ,email:this.Serv.UserName.User ,password:this.Serv.UserName.Pass}).subscribe((data) => {
    if(data.state){
        this.Serv.sendDataEffect(this.Serv.PROGRASSSend_DOM);
        this.Serv.Led[this.ID].name=this.Serv.Led[this.ID].newname;
        this.closeMessage();
      }else{
       this.Serv.goLocation('login');
      }

      });   

  }

  

}
