#!/usr/bin/env node
var debug = require('debug')('node');
var SerialPort = require('serialport');
var app = require('../app');
var CH =require('./ControlHome');


app.set('port', process.env.PORT || 3000);

var server = app.listen(app.get('port'), function() {
  debug('Express server listening on port ' + server.address().port);

});

//--------------------------تعريف السيريل بورت-------------------
 var Readline = SerialPort.parsers.Readline;
 const Ready = SerialPort.parsers.Ready;
 var port = new SerialPort('/dev/ttyACM0', { //COM5 for windows نوع اليورت يختلف من ويندوز او لينكس
  baudRate: 9600,
  Readline:Readline.Readline
	}, function (err) {
		if (err) {
		return console.log('Error: ', err.message);
		}else CH.intilize(port); // بمجرد نجاح الاتصال يتم اعداد البينات من القاعده وارسلها للمتحكمه
});

  // يتم قرأة البيانات من المتحكمه بصيغة سطر سطر وليس حرف حرف
const parser = port.pipe(new Readline({ delimiter: '\r\n' })); 

//حدث يتم تنفيذ الداله في حاله حصل خطأ
port.on('error', function(err) {
    console.log('Error: ', err.message);
})

// يتم تنفيذ الداله في حاله استقبال بيانات من المتحكمه بواسطه السيريل
parser.on('data', function(data){
    console.log(data);
	//-------------------arduino her
	try {
		var command=JSON.parse(data);
		CH.pA(command);
	} catch (e) {
		console.log("not JSON???????");
	}
});

// يتم تنفيذ الداله في حاله اذا تما فتح الاتصال مع المتحكمه
port.on('open', function(err){
    if(!err)
   { console.log("is open");} else  console.log("is errore open"); 
});

// يتم تنفيذ الحدث اذا انقطع الاتصال مع المتحكمه 
port.on('close', function(){
  console.log("is close");	
});
//===========================================================================================================

//---------------------------------اعداد السوكيت ل استقبال اوامر ال اعضاء------------------------------------

// socket io يتم اعداد السوكيت ل استقبال اوامر ال اعضاء
var io=require('socket.io')(server);

//يتم تنفيذ الحدث اذا تم التقاط اتصال جديد بالسرفر او عميل جديد 
io.on('connection',function (socket) {
	console.log("user is connected"); // اظهار رساله

   //يتم تنفيذ الحدث عندما ينقطع الاتصال مع العميل
	socket.on('disconnect', function(){
		console.log('user disconnected'); // اظهار رساله
		//remove clint from array
		CH.setFreeSocketUserSocet(socket); //يتم تحرير المكان لجهاز اخر يتصل بنفس الاسم وكلمه المرور 
	});
	
	// يتم استقبال الرسايل من العملاء عن طريق هذا الحدث
    socket.on('clintuser',function (msg) {
		//heeeeer comeing from user clint
		try {
			var info =JSON.parse(msg); // تحويل الرساله الي نوع جاسون اوبجيكت
			CH.pUC(info,socket); // يتم تمرير الرساله للداله ليتم معالجتها
		} catch (e) {
			
		}
	
	});
	
	// يتم في هذا الحدث تمهيد بيانات المتصل او العميل وهنا يتم اضافه السوكت الخاص بالعميل لبيناته
	socket.on('clintuserlogin',function (msg) {
		try{
			var user_info =JSON.parse(msg); // تحويل النص الي جيسون
			CH.US.getUser().forEach(function(element) {
				if(element.email==user_info.email&&
					element.password==user_info.password){
						element.SOCW=false;
						element.SOC=socket;
						element.SOCT=1;
						element.SOCW=true;
						//console.log(element);
					
				}
			}, this);
		} catch (e) {
			console.log("not JSON");
		}
	});
	
});
//=======================================================================================================

// يتم حذف الاماكن المحرره بعد انتهاء المده التي تجاوزها العميل في انقطاعه عن الاتصال بالسيرفر
setInterval(()=>{
	CH.US.getUser().forEach(function(element) {
		if(! element.SOCW ) 
			if(element.SOCT > 1)
				CH.US.kikUser(element.email)
			else element.SOCT=2;
	}, this);
},9000);

console.log('%c Oh my heavens! ', 'background: #222; color: #bada55');


// لو السيرفر سيتصل بسيرفر اخر
//socket.emit('message', { msg: 'data' });
// var io2=require('socket.io-client');
// var socket2=io2("http://127.0.0.1:4000");
// socket2.on('message', function (data) {
//     console.log(data);
//   });

