import { Component, OnInit } from '@angular/core';
import { ServHttpService } from '../../service/serv-http.service'

@Component({
  selector: 'app-members',
  templateUrl: './members.component.html',
  styleUrls: ['./members.component.css']
})
export class MembersComponent implements OnInit {
  PROGRASSSend_DOM: HTMLElement;

  results:any[];
  constructor(private servhttpservice:ServHttpService){  this.getMembers(); this.servhttpservice.setActiveMenu(6);}

getMembers(){
     this.servhttpservice.servPost('http://'+ServHttpService.ip+':3000/getallmember',{email:this.servhttpservice.UserName.User ,password:this.servhttpservice.UserName.Pass}).subscribe((data) => {
      if(data.state){
        this.results= data.users;
      }else{
       this.servhttpservice.goLocation('login');
      }

      });   

}

clickDelete(id,name){
  let result = confirm("Want to delete "+ name + "?");
  if (result) {
      let user={id:id,optin:"delete",email:this.servhttpservice.UserName.User ,password:this.servhttpservice.UserName.Pass};
      this.servhttpservice.servPost('http://'+ServHttpService.ip+':3000/member',user).subscribe((data) => {
        if(data.state){
          this.servhttpservice.sendDataEffect(this.PROGRASSSend_DOM);
          this.fade( document.getElementById(id));
        }else this.servhttpservice.goLocation('login');
      });
  }
}


clickdActive(id,name){
  let result = confirm("Want to Active "+ name + "?");
  if (result){
      let user={id:id,optin:"active",email:this.servhttpservice.UserName.User ,password:this.servhttpservice.UserName.Pass}; //--------------------------
      this.servhttpservice.servPost('http://'+ServHttpService.ip+':3000/member',user).subscribe((data) => {
        if(data.state){
        this.servhttpservice.sendDataEffect(this.PROGRASSSend_DOM);
          this.fade( document.getElementById("ac"+id));
          }else this.servhttpservice.goLocation('login');
      });        
  }
}

 fade(element) {
  let op = 1;  // initial opacity
  let timer = setInterval(function () {
      if (op <= 0.1){
          clearInterval(timer);
          element.style.display = 'none';
      }
      element.style.opacity = op;
      element.style.filter = 'alpha(opacity=' + op * 100 + ")";
      op -= op * 0.1;
  }, 50);
}




  ngOnInit() {
    this.PROGRASSSend_DOM =document.getElementById("progloadeid");
  }


}

