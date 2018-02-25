# البيت الذكي
### تسجيل الدخول من الهاتف
![login mobe](https://github.com/Nerosoft/smart-home-arabic/blob/master/Smart%20Home%20Screenshot/device-2018-02-17-081355.png)

### تسجيل الدخول من الحاسب
![login pc](https://github.com/Nerosoft/smart-home-arabic/blob/master/Smart%20Home%20Screenshot/login_wep.png)

### الشاشه الخاصه بالادمن فقط في الهاتف
![login pc](https://github.com/Nerosoft/smart-home-arabic/blob/master/Smart%20Home%20Screenshot/device-2018-02-17-081832.png)
![login pc](https://github.com/Nerosoft/smart-home-arabic/blob/master/Smart%20Home%20Screenshot/device-2018-02-17-082310.png)

### الشاشه الخاصه بالادمن فقط في الحاسب
![login pc](https://github.com/Nerosoft/smart-home-arabic/blob/master/Smart%20Home%20Screenshot/Screenshot%20from%202018-02-17%2008-09-19.png)
![login pc](https://github.com/Nerosoft/smart-home-arabic/blob/master/Smart%20Home%20Screenshot/Screenshot%20from%202018-02-17%2007-55-18.png)

# شرح طريقة تسطيب المشروع
1. Arduino
1. Node js
1. Angular
1. Android
## 1. Arduino
* ### _يجب تحميل مكتبة التعامل مع المعلجات المتوازيه بجانب مكتبات اردوينو_
[ArduinoThread](https://github.com/Nerosoft/smart-home-arabic/tree/master/smart%20home%20arduino/arduinoIrmaster)
* ### _يجب تحميل مكتبة التعامل مع حساس اشارة الريموت بجانب مكتبات اردوينو_
[IRremote Arduino Library](https://github.com/Nerosoft/smart-home-arabic/tree/master/smart%20home%20arduino/arduinoIrmaster)
* ### يتم الاتفاق علي المنفذ الذي سيتم تبادل بواسطه البيانات بواسطة هذا الكود
`Serial.begin(9600);`
* ### يجب توصيل الخرج الخاص بحساس الحركه في المنفذ رقم 2 الخاص بالاردوينو حسب الكود
`pinMode(2,INPUT);`
* ### يجب توصيل الخرج الخاص بحساس الريموت في المنفذ رقم 3 الخاص بالاردوينو حسب الكود 
`irrecv=new IRrecv(3);`
* ### تأتي مرحلة الريموت اذا كنت تعرف القيم الخاصه بالريموت الخاص بك فيمكنك التعديل بسهوله علي هذه القيم
```
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
```
* ### اذا لم تكن تعرف فبسهوله يمكنك حرق هذا الكود للكشف عن قيم الريموت الخاص بك
[get_remote_hex](https://github.com/Nerosoft/smart-home-arabic/blob/master/smart%20home%20arduino/get_remote_hex/get_remote_hex.ino)
***
## 2. Node Js
* ### _يجب اولا رفع قاعدة البيانات_
[Mysql Database](https://github.com/Nerosoft/smart-home-arabic/tree/master/sql%20dtatbase)
* ### من جدول اليوزير الحقل جروب
**يتم تحديد عضوية العميل حيث 4 يمثل المدير و 1 يمثل عميل عادي 0 عميل موقوف**
* ### تعديل بيانات الاتصال بالقاعده
```
var con = mysql.createConnection({ // بيانات الاتصال بقاعده البيانات
  host: "localhost", // السيرفر
  user: "root", // اسم المستخدم
  port:  "3305", // المنفذ الخاص بالقعده
  password: "", // كلمه المرور
  database: "node_mysql" // اسم القاعده
});
```
* ### بيانات الاتصال بالادوينو
```
var port = new SerialPort('/dev/ttyACM0', { //COM5 for windows نوع اليورت يختلف من ويندوز او لينكس
  baudRate: 9600, // رقم المنفذ المتفق عليه
  Readline:Readline.Readline
	}, function (err) {
		if (err) {
		return console.log('Error: ', err.message);
		}else CH.intilize(port); // بمجرد نجاح الاتصال يتم اعداد البيانات من القاعده وارسلها للمتحكمه
});
```
* ### يتم تشغيل السيرفر من ملف 
**www.js**
***
## 3. Angular
* ### اعدا الايبي الخاص بالسيرفر من هنا
` static ip:string="127.0.0.1";`
* ### نقوم بعمل استخراج الملفات 
### ng build --prod or ng build
* ### حيث يتم نقل فقط الملفات الي المسار
**inline , main , polyfills , styles , vendor**
### [ang files](https://github.com/Nerosoft/smart-home-arabic/tree/master/Server%20Node%20Js/public/ang)
* ### يتم التعديل هنا علي اسماء الملفات في الاندكس الخاص بالسيرفر
**style**
```
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="icon" type="image/x-icon" href="ang/smart_home.ico">
  <link rel="stylesheet" type="text/css" href="ang/assets/out/layout/css/bootstrap.css">
  <link rel="stylesheet" type="text/css" href="ang/assets/out/layout/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="ang/styles.8022ed9ede8df682ad39.bundle.css">
```
**script**
```
<!-- <script type="text/javascript" src="ang/inline.bundle.js"></script>
<script type="text/javascript" src="ang/polyfills.bundle.js"></script>
<script type="text/javascript" src="ang/styles.bundle.js"></script>
<script type="text/javascript" src="ang/vendor.bundle.js"></script>
<script type="text/javascript" src="ang/main.bundle.js"></script> -->
<!-- ************************** -->
<script type="text/javascript" src="ang/inline.b44cbbd33e8c983b8f53.bundle.js"></script>
<script type="text/javascript" src="ang/polyfills.fed567bc92850d9d52f3.bundle.js"></script>
<script type="text/javascript" src="ang/vendor.cb8e04c57678cf602aa6.bundle.js"></script>
<script type="text/javascript" src="ang/main.ec3238ecc9248c42e715.bundle.js"></script>
```
## 4.Android
* ### اعدا الايبي الخاص بالسيرفر من هنا
`public static String Ip = "192.168.1.5";`
***
**وان شاء الله كلو تمام**
# اذا واجهتك اي مشكله يمكنك مشاهدة الطريقه علي يوتيوب
* ## [youtube](https://www.youtube.com/playlist?list=PLpAujTRyjgcHijNFTWfSNl-dYh-6PBa8Q&playnext)
