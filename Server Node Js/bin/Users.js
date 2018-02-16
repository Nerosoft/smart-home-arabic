var db=require('./DataBase');

var total_request=0; // عداد اتصالات الرسايل القادمه من العملاء
var user_info=new Array(); // بيانات العملء
var Led_Setting_Info; // بيانات الليد 
var Setting_Info; // بيانات الحساسات

module.exports={
    setInfoDash:function(){ // مضاعفه عداد الاتصال
        total_request+=1;
    },
    checkUserDataBase:function(email, callback){ // التحقق من وجود هذا العميل في القاعده قبل انشاؤه
        db.selectOneUser(email, callback);      
    },
    selectOneUserInsert:function(email, callback){ // جلب العميل الذي تم انشاؤه بعد عمليه التسجيل
        db.selectOneUserInsert(email, callback);      
    },
    setUser:function(userinfo){ // تسجيل دخول للمستخدم داخل المصوفه
        user_info.push(userinfo);
       //  console.log(" Array "+user_info);
    },
    findUser_Info:function(key,index='email'){ // جلب عميل من المصفوفه بواسطه اسم العميل 
       return user_info.find(function(user){
         return index=='email' ?  user.email==key :  user.password==key ;
         });
    },
    checkActiveUser:function(user){ // التحقق من ان العميل متصل مع المصفوفه ام لا
        var isOK=true;
         user_info.forEach(function(element) {
             if(user == element.email){
                 isOK = false;
                 return;
             }
         }, this);
         return isOK;
    },
    regsterUser:function(user ,password ,GroupU ,comment,callback){ // انشاء مستخدم جديد
      db.insertUser(user ,password ,GroupU ,comment,callback)  
    },
    selectAllUser:function(callback){ // جلب بينات العملاء من القاعده
        db.selectAllUser(function(err, result, fields){          
            callback(err ,result);
        });
    },
    activeUser:function(id ,callback){ // تفعيل يوزر داخل القاعده
        db.activeUser(id ,callback);
    },
    deleteUser:function(id ,callback){ // حذف عميل او يوزر من القاعده
        db.deleteUser(id ,callback);
    },
    updateUser:function(id ,user ,password ,GroupU ,comment ,callback){ // التعديل علي عميل او يوزر
        db.updateUser(id ,user ,password ,GroupU ,comment ,callback);
    },
    getInfoDash:function(callback){ // جلب بيانات الداش بورد مثل عدد المستخدمين وخلافه
        db.getInfoDash(function(info){
            callback({state:info.state,cont:info,member:user_info.length,req:total_request});
        }); 
    },
    getLemt10User:function(callback){ 
        db.getLemt10User(function(err ,result){ // جلب عشرة مستخدمين من القاعده
            var users={state:false,users:result};
            if (err) callback({state:false}); //throw err;
            callback({state:true,users:result});
            console.log(result.affectedRows + " record(s) 10users");
        });        
    },
    getUser:function(){
        return user_info;
    },
    kikUser:function(kik_user){ // حذف عميل من المصفوفه الخاصه بالعملاء المتصلين
       
        user_info.forEach(function(element) {
            if(element.email==kik_user){
                console.log("kikikikikikik Userrrrr "+kik_user);
              var index = user_info.indexOf(element);
              if (index > -1) {
                user_info.splice(index, 1);
              }
              return;
            }
           }, this);
    },
    changAdminUser:function(newpassword ,NameOfAdmin,callback){ // تغير الباسورد الخاص بالادمن
              db.updateAdminUser(NameOfAdmin ,newpassword ,function(err, result){
                  if(err) callback({state:false});
                   else callback({state:true});
              });
      
    },
    setLedSettingInfo:function(led_setting_info){
        Led_Setting_Info=led_setting_info;
    },
    setSettingInfo:function(setting_info){
        Setting_Info=setting_info;
    },
    getLedSettingInfo:function(){
        return Led_Setting_Info;
    },
    getSettingInfo:function(){
        return Setting_Info;
    },
    setUpdateNameLed:function(id,newname,callback){ // تعديل علي اسم الجهاز الكهربي او الليد
      db.setUpdateNameLed(id,newname,function(err, result){
        if (err) callback({state:false});
        callback({state:true})
        console.log(result.affectedRows + " record(s) updated");
      });  
    },
    getArduinoLed:function(callback){ // جلب بيانات الليد او الجهاز الكهربي
      db.getArduinoLed(function(result){
        callback(result);
      });
    },
    getArduinoSinsor:function(callback){ // جلب بيانات الحساسات
        db.getArduinoSinsor(function(result){
          callback(result);
        });
      }
}