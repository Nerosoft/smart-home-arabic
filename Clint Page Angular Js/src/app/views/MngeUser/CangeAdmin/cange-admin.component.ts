import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ServHttpService } from '../../../service/serv-http.service';

@Component({
  selector: 'app-cange-admin',
  templateUrl: './cange-admin.component.html',
  styleUrls: ['./cange-admin.component.css']
})
export class CangeAdminComponent implements OnInit {
  PROGRASSSend_DOM: HTMLElement;

  user={password:'',newpassword:'',email:''};

  UserError={password:false,newpassword:false};
  constructor(private Serv:ServHttpService,private router:Router){
    this.Serv.setActiveMenu(4); 
    this.user.email=this.Serv.UserName.User;
  }

  ngOnInit() {
    this.PROGRASSSend_DOM =document.getElementById("progloadeid");
  }

  mysubmit(){
    
  
    if( this.user.password.length < 7 || this.user.newpassword.length < 7  ){
        this.UserError.password=true;
        this.UserError.newpassword=true;
        return;
    }
    

             
         
        this.Serv.servPost('http://'+ServHttpService.ip+':3000/cangeadmin',this.user).subscribe((data) => {
            if(data.state){
                this.router.navigateByUrl('/'); 
              }
            else{
                this.UserError.password=true;
                this.UserError.newpassword=true;
            }
         
          });
             
          }
    
    
          click(na){
            switch(na){
              case 'password': this.UserError.password=false;break;
              case 'newpassword': this.UserError.newpassword=false;break;
            }
          }

}
