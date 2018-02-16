var Users=require('./Users'); // عمل اكسبورت لكلاس اليوسر
const util = require('util');
const setsetIntervalPromise = util.promisify(setInterval); // هنا تعريف الدله تتنفذ كل دقيقه لحساب الوقت للتيمر المفتوح
var Port=null; // تعريف متغير للبورت

var Pir_Sensor={FSTI:null,time:"",key:0}; // يتم تخزين بينات حساس الحركه
/*---------------------------------------------Pir_Sensor-------------------------------------------
--(1)FSTI-> يتم حفظ الدالة التي تتنفذ كل ثانيه داخل هذا المتغير عند استلام اشارة حركه جديده        -
--(2)time-> يتم حفظ وقت الانتظار داخل هذا المتغير لبدأ معالجه اشاره جديده                          -
--(3)key -> يتم هنا الاحتفاظ بمفتاح الوضع الخاص بالحساس                                            -
 حيث انه اذا تم تغير هذا الوضع عن طريق اعطاء اوامر جديده للحساس يتم حذف الوضع القديم وتطبيق الجديد -
----------------------------------------------------------------------------------------------------*/

var Led = []; // تعريف متغير يحتوي علي بينات كل ليد او لمبه او جهاز كهربي
/*--------------------------------------------------------------Led---------------------------------------------------------
--(1)id          -> هنا يتم تخذين موقع الليد او الجهاز الكهربي الخاص بالمصفوفه ويبدأ من صفر                                -
--(2)name        -> يتم تخزين هنا اسم الليد او الجهاز الكهربي                                                              -
--(3)state       -> ينم تخذين هنا حاله الجهاز اذا كانت تشغيل ام ايقاف                                                      -
--(4)led         -> يتم تخزين هنا رقم البن الخاص بال المتحكمه                                                              -
--(5)ledinterval -> يتم تخزين هنا الوقت الخاص بالجهاز الكهربي حيث يتم تطبيق وضع الايقاف اوالتشغيل بمجرد انتهاء الوقت       -
--(6)refinterval -> يتم حفظ اخر وقت تم وضعه للجهاز الكهربي اذا تم تطبيق تحديث الوقت مره اخري                               -
--(7)statetime   -> حالة لة الجهاز الكهربي التي سيتم تطبيقها بعد انتهاء الوقت                                              -
--(8)STL         -> حالة التيمر ويقصد بها التيمر يعمل ام لا بمجرد انتهاء التيمر يتم استبعاده                               -
--(9)pir         -> هذا الجهاز منضم لمجموعه اجهزه حساس الحركه ام لا حيث يتم تطبيق بيانات الحساس علي الجهاز اذا استقبل حركه -
----------------------------------------------------------------------------------------------------------------------------*/

var Lingth=4; // رقم البن في المتحكمه الذي سيبدأ اول بن اخراج فولت

var Setting=[{id:0 ,SPIR:false ,time:5,mode:"2",led:[] ,key:1 ,SIR:false}]; // بيانات الحساسات في المتحكمه
/*--------------------------------------Setting----------------
--(1)id   -> ؤقم العنوان لم يستغدم                            -
--(2)SPIR -> الحاله الخاصه بحساس الحركه تشغيل ام ايقاف        -
--(3)time -> الوقت الخاص بالحساس الحركه                       -
--(4)mode -> الوضع الخاص بحساس الحركه                         -
--(5)key  -> تم التعريف من قبل                                -
--(6)SIR  -> حساس الريموت تشغيل ام ايقاف                      -
--------------------------------------------------------------*/

// تمهيد البيانات الخاصه بـ متغير البورت والليد والسيتتينج حيث يتم جلب البيانات من قاعده البيانات
module.exports={
intilize:function initilaizData(port){
    Port=port; // تمهيد البورت
    Users.getArduinoLed(function(result){ //جلب بينات الليد
      Led=result; // تمهيد الليد وتخزين البيانات القادمه من قاعده البيانات داخله
      Users.setLedSettingInfo(Led); //تمرير البينات لملف اليوزير
    });
    Users.getArduinoSinsor(function(result){ // جلب بيانات الحساس من القاعده
      Setting=result;
      Users.setSettingInfo(Setting);
      Setting[0].key=1;
    });
  
    runTimer(); // تشغيل تيمر الوقت الخاص بالاجهزه الكهربيه او الليد
},
pUC: function processUserClint(info ,socket){ // Get message from clints يتم معالجه رسايل اليوزر هنا
   Users.setInfoDash(); // مضاعفة عدد اوامر الاستقبال
   if(valiUser(info)){ // يتم التحقق من بيانات المرسل كلمة السر واسم المستخدم
   
     console.log("successfully info " + info.typemessage); // اظهار رساله

     if(info.typemessage=="led"   || // تشغيل ليد او اطفاء
        info.typemessage=="allled")  // تشغيل كل الاجهزه ام اطفاء الكل
          sendArduino(info.led);    // تمرير الرساله للمتحكمه
     
     else if(info.typemessage=="sensor"){ // تشغيل الحساس ام ايقافه
       if(info.sensor=="ir")  // حساس الريموت
        switch(info.state){
          case "on": sendArduino("lediron:"); break; // تشغيل الحساس
          case "of": sendArduino("ledirstop:");break; // ايقاف الحساس
        }
       else if(info.sensor=="pir") // تشغيل الحساس ام ايقافه
        switch(info.state){ // حساس الحركه
          case "on": sendArduino("ledpiron:"); break; // تشغيل الحساس
          case "of": sendArduino("ledpirstop:");break;  // ايقاف الحساس
        }
       
     }
     else if(info.typemessage=="timeled"){ // امر انشاء او ايقاف تيمر للجهاز الكهربي
          switch(info.STL){ // تحقق من حاله التيمر
            case "on":  // تشغيل التيمر
              setTImer(info.ledinterval, info.id ,info.statetime); // تمهيد بينات التيمر للجهاز الكهربي
              // ابلاغ جميع العملاء بحالة هذا التيمر
              callAllUser({msg:"timeled" , ledinterval:info.ledinterval , refinterval:info.refinterval , id:info.id , statetime:info.statetime , STL:"on"});
            break

            case "of": // ايقاف التيمر
              setTImer("0" ,info.id ,info.statetime); // تمهيد بينات التيمر للجهاز الكهربي
                // ابلاغ جميع العملاء بحالة هذا التيمر
              callAllUser({msg:"timeled" , ledinterval:Led[info.id].ledinterval , id:Led[info.id].id , statetime:Led[info.id].statetime , STL:"of"});
            break
  
            case "ofall": // ايقاف كل التيمر
            Led.forEach(function(element) { // تصفير متغير التيمر 
              element.ledinterval="0";
            }, this);
              callAllUser({msg:"timeled",ledinterval:"",id:"",statetime:"",STL:"ofall"}); // تبليغ العملاء
            break
          }
     }
     else if(info.typemessage=="option_pir"){ // امر لحساس الحركه
       if(info.state=="send"){ // الحاله ارسال بيانات
        Setting[0].time=info.time; // تخزين الوقت القادم من العميل
        Setting[0].mode=info.mode; // تخزين الوضع
          let new_led=JSON.parse(info.led); // تحويل متغيل الليد الي مصفوفه
          new_led.forEach(function(element,index) { 
            Led[index].pir=element;   // تخزين الاجهزه التابعه للحساس الحركه
         }, this);

        Setting[0].key+=1; // يتم تغير المفتاح القديم الي مفتاح جديد ليتم تظبيق الوضع الجديد
        // تبليغ جميع المستخدمين بهذا الامر 
        callAllUser({msg:"option_pir" , time:Setting[0].time , mode:Setting[0].mode , led:info.led});
       }else if(info.state=="get"){
        callAllUser({ msg:"option_pir", time:Setting[0].time , mode:Setting[0].mode ,led:'['+info.led+']'});
       }
     } 
     else if(info.typemessage=="kik"){ // استلام لامر طرد مستغدم
       console.log("kik User  " +JSON.stringify(info)); // طباعه
      callAllUser({msg:"kik" ,id:info.id ,name:info.name }); // تبليغ بأنه سيتم طرد العميل
      Users.kikUser(info.name); // يتم طرد العميل
      
     }
  }else console.log("erooooooooooor"); // لو حدث مشكله في عمليه التحقق من البيانات
},

// يتم هنا معالجه الرسايل القادمه من اردينو
pA: function processArduino(command){ // her -- reseved from arduino 
       
        if(command.msg=="ledon"  || 
            command.msg=="ledof" ){ // يتم الدخول اذا كانت الرساله تشغيل او اطفاء جهاز كهربي
            Led[(command.numb-Lingth)].state=(command.msg=="ledon")?"on":"of" // وضع الحاله في متغير الليد
              callAllUser(command);          // تبليغ العملاء    
          }
        else if(command.msg=='ledallrun' ||
            command.msg=='ledallstop' ){ // يتم الدخول اذا كانت الرساله تشغيل او اطفاء كل  الاجهزه كهربي
            let state = (command.msg=="ledallrun")?"on":"of" // تخزين الحاله للاجهزه
            Led.forEach(function(element) { // يتم حفظ الحاله الجديده سواء اطفاء او تشغيل داخل المتغير الرأيسي
              element.state=state;
            }, this);
            callAllUser(command); // تبليغ العملاء بهذا الوضع
          }
        else if (command.msg=='ledpiron' ||
            command.msg=='ledpirstop' ){ // يتم هنا تخزين حالة الحساس الخاص بالريموت
              Setting[0].SPIR=(command.msg=='ledpiron')?true:false;
              callAllUser(command);
            }
        else if(command.msg=='lediron'    ||
            command.msg=='ledirstop'){    // يتم هنا تخزين حاله الحساس الخاص بالحركه
              Setting[0].SIR=(command.msg=='lediron')?true:false;
              callAllUser(command);
            }else if(command.msg=="is ranning"){ // هنا يتم ارسال بينات للمتحكمه عند استلام هذه الرساله من اردوينو
                Led.forEach(element => {
                  if(element.state=="on") // اذا كانت الحاله التي تم جلبها من قاعده البيانات داخل المتغير الخاص باليد تشغيل
                    sendArduino("ledon"+element.led+":") // يتم ارسال امر تشغيل لهذا الليد
                });
                sendArduino(Setting[0].SIR==1?"lediron:":"ledirstop:"); // تمهيد حساس الريموت داخل المتحكمه
                sendArduino(Setting[0].SPIR==1?"ledpiron:":"ledpirstop:"); // تمهيد حساس الحركه داخل المتحكمه
            }
      
        else if(command.msg=='IRDATA'){ // يتم الدخول اذا استجاب الحساس للحركه 
          
          // معالجه البيانات الخاصه بحساس الحركه 
          if( Pir_Sensor.time<=0 || Pir_Sensor.key != Setting[0].key ){
            console.log("IIIIIIRRRRRRR");
            clearInterval(Pir_Sensor.FSTI);
            Pir_Sensor.key=Setting[0].key;
            Pir_Sensor.time=Setting[0].time;
            pirStart(Setting[0].mode);
            Pir_Sensor.FSTI=setInterval(function(){
              Pir_Sensor.time-=1;
              if(Pir_Sensor.time<=0){
                  pirend(Setting[0].mode);
                  clearInterval(Pir_Sensor.FSTI);
                }
            },1000);
          }
        }

},
callAllUser:callAllUser,


US:Users,
//يتم تحرير المكان لجهاز اخر يتصل بنفس الاسم وكلمه المرور 
setFreeSocketUserSocet:function(UserSocket){
  //console.log(UserSocket);
  Users.getUser().forEach(function(element) {
      if(element.SOC==UserSocket){
        element.SOCW=false;
        return;
      }
     }, this);
},
getAllSettingLed:function(){
  return Led; 
}


};

function valiUser(js){              // checking  user and password is sign or not
  var Vuser = Users.findUser_Info(js.email); // searching in array by email user
    console.log("the user is " + Vuser);
  if((Vuser+"")!='undefined')               //checking the result if it was equales undefined or value
    return Vuser.email==js.email && Vuser.password==js.password?true:false; // if name and password true return true or false
    else                                  // if value or result is not info return false
    return false;
}

// يتم مراسلة المستخدمين
function callAllUser(MSG){
  MSG=JSON.stringify(MSG);
  Users.getUser().forEach(function(element) {  
    if( element.SOCW)         
    element.SOC.emit('message', { msg : MSG           
    });}, this);   
}
// يتم مراسله اردوينو
function sendArduino(msg){
  Port.write(msg, function(err) {
    if (err) 
      return console.log('Error on write: ', err.message);
      console.log('message written');
  });
}
// يتم هنا تمهيد التيمر للجهاز الكهربي
function setTImer(ledinterval ,id ,statetime){
  Led[id].statetime=statetime;
  Led[id].ledinterval=ledinterval;
  Led[id].refinterval=ledinterval;
  Led[id].STL=(ledinterval=="0")?"of":"on"

}
// يتم هنا معالجه التيمر الخاص بالاجهزه الكهربيه
function runTimer(){
  setsetIntervalPromise(()=>{
    Led.forEach(function(element) {
      if(element.ledinterval > 0){
          element.ledinterval-=1;
          console.log('the---> '+element.ledinterval);
          if(element.ledinterval != 0)
          callAllUser({msg:"timeled",ledinterval:element.ledinterval , id:element.id , statetime:element.statetime , STL:"on"});
          if(element.ledinterval <= 0){
             sendArduino("led" + element.statetime + element.led + ":");
             element.STL="of"
             callAllUser({msg:"timeled" , ledinterval:element.ledinterval , id:+element.id , statetime:element.statetime , STL:"of"});
            }
      }
    }, this);
  },60000);
}

// يتم تنفيذ هذه الداله عند استلام حركه من الحساس
function pirStart(mode){ 
  switch (mode){
    case "1": pirSetModeLed("on"); break;    //on---off تشغيل الجهاز اذا كان الوضع 1
    case "2": pirSetModeLed("on"); break;   //on  تشغيل الجهاز اذا كان الوضع 2
    case "3": pirSetModeLed("of"); break;  //off ايقاف الجهاذ اذا كان الوضع 3
    case "4": pirSetModeLed("of"); break; // off----on ايقاف الجهاز اذا كان الوضع 4
  }
}
// يتم تنفيذ هذه الداله عند  انتهاء مرورالوقت الخاص بالحساس
function pirend(mode){
  switch (mode){
    case "1": pirSetModeLed("of"); break;    //on---off اذا كان الوضع 1 يتم اطفاء الجهاز 
    case "4": pirSetModeLed("on"); break; // off----on اذا كان الوضع 4 يتم تشغيل الجهاذ
  }
}

// تشغيل او اطفاء الاجهزه التي تنضم لحساس الحركه
function pirSetModeLed(state){
  var pir_Led = [];
  Led.forEach(function(element) {
    if(element.pir)pir_Led.push(element.led);
  }, this);
  if(state=="on")
    pir_Led.forEach(function(element) {
       sendArduino("ledon"+element+":");
       console.log("ON LED = " + element);
    }, this);    
  else if(state=="of")
    pir_Led.forEach(function(element) {
      sendArduino("ledof"+element+":");
      console.log("OFF LED = " + element);
    }, this);
}


//User Json ===>
//{"email":"Nero","password":"123","typemessage":"led","led":"ledon4:"} ==>led on or off
//{"email":"Nero","password":"123","typemessage":"led","led":"stateforallled:"} ==>State For All Led
//{"email":"Nero","password":"123","typemessage":"timeled" ,"led":"8" ,"ledinterval":"1" ,"statetime":"on","STL":"on"} ==> time of led on or of


// led={

// {id: 0, name:'Living Room1',state:"of", led:'4',ledinterval:"0",refinterval:"0",statetime:"of",STL:"of",pir:false},
  // {id: 1, name:'Living Room2',state:"of", led:'5',ledinterval:"0",refinterval:"0",statetime:"of",STL:"of",pir:false},
  // {id: 2, name:'Living Room3',state:"of", led:'6',ledinterval:"0",refinterval:"0",statetime:"of",STL:"of",pir:false},
  // {id: 3, name:'Living Room4',state:"of", led:'7',ledinterval:"0",refinterval:"0",statetime:"of",STL:"of",pir:false},
  // {id: 4, name:'Living Room5',state:"of", led:'8',ledinterval:"0",refinterval:"0",statetime:"of",STL:"of",pir:false},
  // {id: 5, name:'Living Room6',state:"of", led:'9',ledinterval:"0",refinterval:"0",statetime:"of",STL:"of",pir:false},
  // {id: 6, name:'Living Room7',state:"of", led:'10',ledinterval:"0",refinterval:"0",statetime:"of",STL:"of",pir:false},
  // {id: 7, name:'Living Room8',state:"of", led:'11',ledinterval:"0",refinterval:"0",statetime:"of",STL:"of",pir:false},
  // {id: 8, name:'Living Room9',state:"of", led:'12',ledinterval:"0",refinterval:"0",statetime:"of",STL:"of",pir:false}

// }