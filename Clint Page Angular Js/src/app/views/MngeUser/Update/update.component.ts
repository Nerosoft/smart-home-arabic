import { Component, OnInit } from '@angular/core';
import { ServHttpService } from '../../../service/serv-http.service';
import {Router ,ActivatedRoute,Params} from '@angular/router';

@Component({
  selector: 'app-update',
  templateUrl: './update.component.html', // templateUrl: './update.component.html',
  styleUrls: ['./update.component.css']
})
export class UpdateComponent  implements OnInit {
  PROGRASSSend_DOM: HTMLElement;
  StateOfUser:string='Active';
  submit:string='Update';

  user  ={id:'',GroupU:'',user:'',email:'', password:'----------',
  passwordConfirmation:'----------',phone:'',comment:''};

  UserError={user:false,email:false, password:false,phone:false};

  user2={user:'',email:'', password:'',
  passwordConfirmation:'',phone:'',comment:'',state:'',error:[]};
  
  constructor(private servhttpservice:ServHttpService,private router:Router , private route:ActivatedRoute){
      this.route.params.subscribe((params:Params) => {
      this.user.id=params.id;
      this.user.GroupU=params.GroupU;
      this.user.user=params.user;
      this.user.comment=params.comment;
      this.clickdrop( this.user.GroupU);
      });
  }
  ngOnInit() { 
    this.PROGRASSSend_DOM =document.getElementById("progloadeid");
    this.servhttpservice.sendDataEffect( this.PROGRASSSend_DOM  );
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

      let user={user:this.user,optin:"update",state:"",email:this.servhttpservice.UserName.User ,password:this.servhttpservice.UserName.Pass}; //--------------------------
      this.servhttpservice.servPost('http://'+ServHttpService.ip+':3000/member',user).subscribe((data) => {
        if(data.state){
        this.servhttpservice.sendDataEffect(this.PROGRASSSend_DOM);
        this.router.navigateByUrl('/member'); 
        }else this.servhttpservice.goLocation('login');
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
            case '1' :   this.user.GroupU='1'; this.StateOfUser='Active';break;
            case '0' :   this.user.GroupU='0'; this.StateOfUser='Wait';break;
          }
      }

}
