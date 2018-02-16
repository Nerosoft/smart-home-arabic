import {  HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
export class htp {
   
    constructor( private http:HttpClient) { }
public aa="adas";
data : Observable<any>;
    results:any;
    servPost(url:string){}
    
    servGet(url:string):any{
  this.data=new Observable(observable=>{
    this.http
    .get(url) .subscribe((data)=>{
        observable.next( data);
    });

  });
     
     
      return this.data;
    }
  

}