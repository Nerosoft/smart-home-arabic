import { Component, OnInit } from '@angular/core';
import { ServHttpService } from '../../../service/serv-http.service';
import {Router} from '@angular/router';
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  PROGRASSSend_DOM: HTMLElement;

  StateOfUser:string='Active';
  submit:string='Register';
  results:any={user:'',age:'',city:''};
  user={user:'',email:'', password:'',
  passwordConfirmation:'',phone:'',comment:'',groupid:'1'};

  UserError={user:false,email:false, password:false,phone:false};

  user2={user:'',email:'', password:'',
  passwordConfirmation:'',phone:'',comment:'',state:'',error:[]};

  constructor(private Serv:ServHttpService,private router:Router){this.Serv.setActiveMenu(5);}
  ngOnInit() {
    this.PROGRASSSend_DOM =document.getElementById("progloadeid");
  }


  mysubmit(){

    if(this.user.user.length < 5){
      this.UserError.user=true;
      return;
     }

     if( this.user.password.length < 7 || (!(this.user.password == this.user.passwordConfirmation)) ){
      this.UserError.password=true;
      return;
     }


     
      this.Serv.servPost('http://'+ServHttpService.ip+':3000/regg',{adduser:this.user,email:this.Serv.UserName.User,password:this.Serv.UserName.Pass}).subscribe((data) => {
        if(data.state){
            this.Serv.sendDataEffect( this.PROGRASSSend_DOM  );
            this.router.navigateByUrl('/member'); 
          }
        else{
          if(data.user)
            this.UserError.user=true;
          else this.Serv.goLocation('login');
        }
     
      });
         
      }


      click(na){

        switch(na){
          case 'user' :    this.UserError.user=false;break;
          case 'email':    this.UserError.email=false;break;
          case 'password': this.UserError.password=false;break;
          case 'phone':    this.UserError.phone=false;break;
        }

      }

      clickdrop(na){

          switch(na){
            case '1' :   this.user.groupid='1'; this.StateOfUser='Active';break;
            case '0' :   this.user.groupid='0'; this.StateOfUser='Wait';break;
          }
      }

}




