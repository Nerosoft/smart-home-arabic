#include <IRremote.h> // استدعاء المكتبه الخاصه باتعامل مع اشاره الريموت
#include <Thread.h> // استدعاء المكتبه الخاصه بالمعلجات المتوازيه
#include <ThreadController.h> // المكتبه الخاصه بالتحكم في المعالجه المتوازيه

Thread myThread = Thread(); // تعريف معالجه متوازيه
ThreadController controll = ThreadController(); // تعريف كائن للتحكم في المعالجه المتوازيه
void initializThread(void (*callback)(void) , int interval); // تعريف الفانكشن التي تتعامل مع الحساسات
IRrecv *irrecv; //تعريف متغير للتعامل مع الريموت
boolean IRSTATE =true;  // الحاله الخاصه بالريموت يعمل ام لا
//variubal for setting the sensor
boolean PRISTATE = true; // الحاله الخاصه بحساس الحركه

// تمهيد العمل
void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600); // تفعيل السيريال والمنفذ والذي سيتم من خلاله تواصل البيانات مع السيرفر
  while (!Serial); // wait for serial port to connect. Needed for native USB port only
  startPinOutPut();//for out and input Pin تمعيد المناذ الخاصه بالمتحكمه
  initializThread(checkDigitalAnlog,250); // تفعيل المعالجه المتوازيه الخاصه بالحساسات
  Serial.println("{\"msg\":\"is ranning\"}"); // ارسال جمله للسيرفر بأن تم تفعيل العمل بنجاح
}

void loop() {
  // put your main code here, to run repeatedly:
  processSerial(); // قرائة بيانات من السيرفر
  controll.run(); // تشغيل المعالجه المتوازيه
}



void processSerial(){ // for process message comeing from node
    String command=readDataFromNode(); //  قرأة البيانات من السيرفر
    if(command!=""){                  //اذا تم قرأة بيانات يتم دخول البلوك
     // Serial.println(command);
       if(command.indexOf("ledon")>-1)              //   تفعيل خرج بمقدار خمسه فولت
          ledHigh(subNumper(command , "ledon"));
       else if(command.indexOf("ledof")>-1)          // اغلاق الخرج الخاص بالبن المرسل
          ledLow(subNumper(command , "ledof")); 
       else if(command.indexOf("ledpiron")>-1){   // تفعيل حساس الحركه
         PRISTATE=true;                            // تغير حالة الحساس
         Serial.println("{\"msg\":\"ledpiron\"}"); // ابلاغ السيرفر
       }
       else if(command.indexOf("ledpirstop")>-1){ // ايقاف حساس الحركه
         PRISTATE=false;                                // تغير الحاله
         Serial.println("{\"msg\":\"ledpirstop\"}");  // ابلاغ السيرفر
       }
       else if(command.indexOf("lediron")>-1){         // تفعيل حساس الريموت
         IRSTATE=true;                              // تغير الحاله
          Serial.println("{\"msg\":\"lediron\"}");  // تبليغ السيرفر
       }
       else if(command.indexOf("ledirstop")>-1){   // ايقاف حساس الريموت
         IRSTATE=false;                               // تفير الحاله
         Serial.println("{\"msg\":\"ledirstop\"}");  // ابلاغ السيرفر
       }
       else if(command.indexOf("ledallrun")>-1){       // تفعيل كل البناتخمسه فولت
         ledAllOffAndOn(true);                         // تفعيل الكل
         Serial.println("{\"msg\":\"ledallrun\"}"); // تبليغ السيرفر
       }
       else if(command.indexOf("ledallstop")>-1){     // ايقاف خرج البنات 
         ledAllOffAndOn(false);                      // ايقاف الخرج
          Serial.println("{\"msg\":\"ledallstop\"}");  // تبليغ السيرفر
       } 
       else if(command.indexOf("stateforallled")>-1)     //   جلب حاله البنات والحساسات
          Serial.println(stateForAllLed());          //
       
         command="";
      }
 }
  
  // قراءة بيانات من السيرفر بواسطه السيريال
String readDataFromNode(){
  String com="";
  if(Serial.available()>0) {
    int infinty=0;
    while(1){
      if(Serial.available()>0){
         char received = Serial.read();
         com.concat(received);
         infinty=0;
         if(received==':')
         return com;
      }
      if(++infinty>1000)
      break;
     }
   }//end while
   return "";
}//end function


//--------- start led output تمهيد المنافذ الخاصه بالمتحكمه
void startPinOutPut(){
    for(int i=4;i<=12;i++)
       pinMode(i,OUTPUT);
       
    pinMode(2,INPUT); // for reseved volut from pir  يتم توصيل خرج حساس الحركه في المنفذ رقم 2
    irrecv=new IRrecv(3); // for reseved volut from ir يتم توصيل خرج حساس الرموت في هذا المنفذ رقم 3
    irrecv->enableIRIn(); // Start the receiver 
  }//end function

  //---------start High and Low led--------------- تفعيل منفذ او بن
 void ledHigh(int numled){
      if(numled>=4&&numled<=12){
      digitalWrite(numled,HIGH);
     // LedOn[numled-23]=numled;
     Serial.println("{\"msg\":\"ledon\",\"numb\":\"" + String(numled) + "\"}");
      }
  }

// ايقاف المنفذ
 void ledLow(int numled){ 
       if(numled>=4&&numled<=12){
      digitalWrite(numled,LOW);
     // LedOn[numled-23]=0;
      Serial.println("{\"msg\":\"ledof\",\"numb\":\"" + String(numled) + "\"}");
      }
  }

 void ledAllOffAndOn(boolean State){ // ايقاف او تفعيل خرج كل المنافذ
    if(State)
      for(int i=4; i<=12; i++)
         digitalWrite(i,HIGH);
    else
     for(int i=4; i<=12; i++)
          digitalWrite(i,LOW);    
  } 
  
 int subNumper(String command , String index){ // استخراج رقم البن من الرساله الخاصه بالسيرفر
      String getnump = "";
      int uu=command.indexOf(index);
      getnump += command.charAt(uu+5);
      getnump += command.charAt(uu+6);
     // Serial.println(getnump.toInt());
      return getnump.toInt();
  }
//----------end start High and Low led------------

//--------------Get All Led State---------------------
String stateForAllLed(){// get all stats for pin to led
     String JSON = "{\"msg\":\"stateforallled\",\"ledmain\":[{\"pir\":\"" + String(PRISTATE) + "\",\"ir\":\"" + String(IRSTATE) + "\"}";
     for(int i=4;i<=12;i++)
         JSON+= ",{\"led\":\"" + String(digitalRead(i)) + "\"}";
     JSON+="]}";
//     Serial.println(JSON);
     return JSON;
  }
  
 //--------------initializ thread----------------------           تمهيد المعالجه المتوازيه للحساسات
 void initializThread(void (*callback)(void) , int interval){
      myThread.onRun( callback );
      myThread.setInterval( interval );
      controll.add(&myThread);
      myThread.enabled = true;
  } 
  
  
//--------------------------Start IR------ تشغيل الحساسات
  int loop_pir=0;
  void checkDigitalAnlog(){
    decode_results results;
      if ( irrecv->decode(&results) && IRSTATE ) {//------------------------------------------
         // Serial.println(results.value, HEX);
          irOnOrder(results.value);   
          irrecv->resume(); // Receive the next value
      }
      if(PRISTATE){
        if(digitalRead(2) && loop_pir == 0){
           Serial.println("{\"msg\":\"IRDATA\"}");
           loop_pir=1;
         }
      }
      // Serial.println( loop_pir );
       if( loop_pir != 0) {
          loop_pir++; 
          if(loop_pir>=20)
              loop_pir=0;
        }
   }

  void irOnOrder(int hexcode){ // مهم جدا القيم الخاصه بقيم الريموت المستخدم
      switch(hexcode){
          case 0x205D906F : if( digitalRead(4) ) ledLow(4); else ledHigh(4);  break;
          case 0x205DB847 : if( digitalRead(5) ) ledLow(5); else ledHigh(5);  break;
          case 0x205DF807 : if( digitalRead(6) ) ledLow(6); else ledHigh(6);  break;
          case 0x205DB04F : if( digitalRead(7) ) ledLow(7); else ledHigh(7);  break;
          case 0x205D9867 : if( digitalRead(8) ) ledLow(8); else ledHigh(8);  break;
          case 0x205DD827 : if( digitalRead(9) ) ledLow(9); else ledHigh(9);  break;
          case 0x205D8877 : if( digitalRead(10) ) ledLow(10); else ledHigh(10);  break;
          case 0x205DA857 : if( digitalRead(11) ) ledLow(11); else ledHigh(11);  break;
          case 0x205DE817 : if( digitalRead(12) ) ledLow(12); else ledHigh(12);  break;
        }
  }
//--------------------End Sensore Ir--
