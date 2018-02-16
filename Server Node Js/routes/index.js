var express = require('express');
var router = express.Router();
var qs=require('querystring');
var url=require('url');
var Users=require('../bin/Users');


// لعمل اتصال بواسطه http//:
router.use(function(req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
  next();
});




/* GET home page. */
router.get('/', function(req, res) { 
  //res.render('index', { title: 'Express',name:req.session.user });
  if(req.session.user ){ // التأكد من ان العميل لديه جلسه
    User=Users.findUser_Info(req.session.user); // جلب العميل من مصفوفه المعملاء
    if(User)//ارسال صفحه الانكولار
      res.render('index', { user:req.session.user ,pass:req.session.pass ,state:(User.GroupU==4)?"admin":"user" });
    else req.session.destroy(function(err) {
      if(err) {
        console.log(err);
      } else {
        res.redirect("/login"); // ان وجد خطأ في تسجيل الدخول 
      }
    });
  }
  else 
    res.redirect("/login"); // ان وجد خطأ في تسجيل الدخول 
});

// /* GET Login page. */
router.post('/login', function(req, res) { // عملية تسجيل الدخول

  if(req.method=='POST' && Users.checkActiveUser(req.body.user)){ // العميل ميكونش اكتف والا لن بستطيع الدخول


      Users.checkUserDataBase(req.body.user,function(user){//لو المستخدم مكنش في القاعده البيانات
        if(user[0] && req.body.user==user[0].email && req.body.pass==user[0].password){  // لو المستخدم موجود اللاسم والباسورد مظبوطيت
          req.session.user=req.body.user; // حفظ اسم المستخدم في السيشن
          req.session.pass=req.body.pass; // حغظ اسم كلمة المرور في السيشن
          
          Users.setUser(user[0]); // تمرير المستخدم لمصفوفه الاكتف او المتصلين حاليا
                
          res.redirect("/");  // عمل اعادة توجيه
        }else res.redirect("/login");
        
      });
   
  } else res.redirect("/login"); 
  
});

router.get('/login', function(req, res) {
  if( req.session.user ) 
  res.redirect("/");
  else
    res.render('LoginUser');
});

// تسجيل خروج
router.get('/logout', function(req, res) {  
  // if the user logs out, destroy all of their individual session
  // information
  var userlogout=req.session.user; 
	req.session.destroy(function(err) {  // تدمير الجلسه
		if(err) {
			console.log(err);
		} else {
      Users.kikUser(userlogout); //clear User From Array Login مسح الحميل من مصفوفة التسجيل
      res.redirect("/login");
    }
  });
 
});


//for admin جلب العملاء من القاعده
router.post('/getallmember', function(req, res) {
  if(req.method=='POST' && valiUser(req.body.email,req.body.password,4)) // 4 ---- ترمز بأن المسؤل عن هذا الاتصال يجب انا يكون ادمن
    Users.selectAllUser(function(err ,result){
      if (err) res.json({state:false});
      else res.json({state:true,users:result});
    });
  else res.json({state:false});
});

/* POST register page. */ //for admin عمليه انشاء عميل
router.post('/regg', function(req, res) {
  if(req.method=='POST'&& valiUser(req.body.email,req.body.password,4)){ // 4 ---- ترمز بأن المسؤل عن هذا الاتصال يجب انا يكون ادمن
    Users.checkUserDataBase(req.body.adduser.user,function(user){ // التحقق اولا من انه لا يوجد هذا الاسم
      if(!user[0]){ // is not empty يتم عمل انشاء للعميل داخل القاعده
        Users.regsterUser(req.body.adduser.user ,req.body.adduser.password ,req.body.adduser.groupid ,req.body.adduser.comment ,function(err ,result){
          if (err) console.log( err);
          
          Users.selectOneUserInsert(req.body.adduser.user,function(result){ // جلب العميل من القاعده
            console.log("1 record inserted "+JSON.stringify(result));
            res.json({state:true,newuser:result[0]});
          })
          
        });
       }else{
         res.json({state:false ,user:true});     
       }
    });
  }else res.json({state:false,user:false});
});


/* POST edite , active , delete page. */ //for admin
router.post('/member', function(req, res) {
  if(req.method=='POST'&& valiUser(req.body.email,req.body.password,4)){ // 4 ---- ترمز بأن المسؤل عن هذا الاتصال يجب انا يكون ادمن
      
    switch (req.body.optin) {
      case "active":
        Users.activeUser(req.body.id ,function (err, result){ // عمل تنشيط للمستخدم
          if (err) res.json({state:false});
          console.log(result.affectedRows + " record(s) active");
          res.json({state:true});
        });
        break;

      case "delete":
        Users.deleteUser(req.body.id ,function (err, result){ // حذف عميل
          if (err) res.json({state:'false'});
          console.log(result.affectedRows + " record(s) delete");
          res.json({state:true});
        });
        break; 

      case "update":  // تعديل بيانات عميل
        Users.updateUser(req.body.user.id ,req.body.user.user ,req.body.user.password ,req.body.user.GroupU ,req.body.user.comment ,function (err, result){
          if (err) { console.log("errrrrrrrrr \n" + err);    res.json({state:false});}
          console.log(" record(s) Update");
          res.json({state:true});
        });
        break; 
    
    }

  }else res.json({state:false});
});

// total member panding member total connction totla request  // for admin
// جلب عدد الاعضاء والاتصالات 
router.post('/getdash', function(req, res) {
  if(req.method=='POST' && valiUser(req.body.email,req.body.password,4)) // 4 ---- ترمز بأن المسؤل عن هذا الاتصال يجب انا يكون ادمن
    Users.getInfoDash(function(info){
      if(info.state)
        res.json(info);
      else res.json({state:false});
    });
  else res.json({state:false});
});

// for admin جلب عشرة عملاء للادمين
router.post('/get10user', function(req, res) {
  if(req.method=='POST' && valiUser(req.body.email,req.body.password,4)) // 4 ---- ترمز بأن المسؤل عن هذا الاتصال يجب انا يكون ادمن
    Users.getLemt10User(function(Lusers){
    if(Lusers.state)
        res.json(Lusers);
    else res.json({state:false});
    });
  else res.json({state:false});
});

//conction Users for admin جلب العملاء المتصلين
router.post('/getactiveuser', function(req, res) {
  //&& valiUser(req.body.email,req.body.password,4)
  if(req.method=='POST' && valiUser(req.body.email,req.body.password,4) ){ // 4 ---- ترمز بأن المسؤل عن هذا الاتصال يجب انا يكون ادمن
    var new_users=new Array();
    Users.getUser().forEach(function(element) {
      if(element.GroupU!=4)
        new_users.push({id:element.id,email:element.email});
    }, this);
    res.json({state:true,users:new_users});
  }else res.json({state:false});
});

//------------------------------------------------------------------------------------------------------------------

//mob get all dash for admin هنا يتم جلب البيانات الخاصه بلادمن للاندرويد وهي الداش بورد
router.post('/mopgetalldash', function(req, res) { 
  if(req.method=='POST' && valiUser(req.body.email,req.body.password,4)){ // 4 ---- ترمز بأن المسؤل عن هذا الاتصال يجب انا يكون ادمن
    var new_users=new Array();
    Users.getUser().forEach(function(element) { // جلب العملاء المتصلين
      if(element.GroupU!=4){
        new_users.push({
          id: element.id,
          GroupU: element.GroupU,
          comment: element.comment,
          email: element.email } );
      }
    }, this);

    Users.getInfoDash(function(info){ // جلب عدد العملاء وعدد الاتصالات وعدد المستخدمين ال غير نشطين
      if(info.state){

          Users.selectAllUser(function(err ,result){ // جلب العملاء
            if (err) res.json({state:false});
            else { //end
              var pir=[];
              Led:Users.getLedSettingInfo().forEach(function(element) { // جلب الاجهزه التي تنضم للحساس
                pir.push({pir:element.pir})
              }, this);
              // ارسال الطلب
              res.json({state:true,useractive:new_users ,info:info ,users:result,Setting:Users.getSettingInfo(),pir});
            } 
          });
       
        } 
      else res.json({state:false});
    });

  }else res.json({state:false});
});

// تسجيل الدخول للعميل من علي الهاتف
router.post('/moplogin', function(req, res) {
 
  if( req.method=='POST' && Users.checkActiveUser(req.body.user) ){
  

    Users.checkUserDataBase(req.body.user,function(user){//لو المستخدم مكنش في القاعده البيانات

      if( user[0] && req.body.user==user[0].email && req.body.pass==user[0].password){ // لو المستخدم موجود اللاسم والباسورد مظبوطيت

        Users.setUser(user[0]);
        res.json({state:true,GroupU:(user[0].GroupU==4)?"admin":"user"});//true
       
      }else res.json({state:false});//false
        
      
    });



  }else res.json({state:false});
});

// تسجيل خروج للعميل من علي الهاتف
router.post('/moplogout', function(req, res) {
  if(req.method=='POST' && valiUser(req.body.email,req.body.password,1)){ // 1 -- الكل يصل لهذا الطلب
    Users.kikUser(req.body.email); //clear User From Array Login
    res.json({state:true});
  }
  else res.json({state:false});
});

//-----------------------------------------------------------------------------------------------------------------

//dash changadmin Pirsensor member reg --------------------//
router.get('/dash', function(req, res) {
  res.redirect("/");
});
router.get('/changadmin', function(req, res) {
  res.redirect("/");
});
router.get('/Pirsensor', function(req, res) {
  res.redirect("/");
});
router.get('/member', function(req, res) {
  res.redirect("/");
});
router.get('/reg', function(req, res) {
  res.redirect("/");
});
//**********************************************************//

//chang admin password// تغير كلمه السر
router.post('/cangeadmin', function(req, res) { 
  if(req.method=='POST' && valiUser(req.body.email,req.body.password,4)){ // 4 ---- ترمز بأن المسؤل عن هذا الاتصال يجب انا يكون ادمن
    Users.changAdminUser(req.body.newpassword,req.body.email,function(result){
      res.json(result);
    });
  } else res.json({state:false});
});

// جلب بيانات الليد وبيانات الحساسات
router.post('/settingLed', function(req, res) {
  if(req.method=='POST' && valiUser(req.body.email,req.body.password,1)){ // 1--- الكل يصل لهذا الطلب
    res.json({state:true,Led:Users.getLedSettingInfo() ,Setting:Users.getSettingInfo()});
  }
  else res.json({state:false});
});

// تغير اسم الليد
router.post('/updateledname', function(req, res) {  //For Admin
  if(req.method=='POST' && valiUser(req.body.email,req.body.password,4)) // 4 ---- ترمز بأن المسؤل عن هذا الاتصال يجب انا يكون ادمن
    Users.setUpdateNameLed(req.body.id,req.body.newname,function(data){
      if(data.state){
        Users.getLedSettingInfo()[req.body.id].name=req.body.newname;
        res.json(data);
      }else res.json(data);
    });
  else res.json({state:false});
});

module.exports = router;

// التحقق من العميل
function valiUser(email,password,validGruop){              // checking  user and password is sign or not         

  var Vuser = Users.findUser_Info(email); // searching in array by email user
  if((Vuser+"")!='undefined')               //checking the result if it was equales undefined or value
    if( Vuser.email==email && Vuser.password==password){
        if(validGruop==4)
         return Vuser.GroupU==validGruop?true:false;
        
        return true; // if name and password true return true or false
    }       
    else  return false;   // if value or result is not info return false 
}
