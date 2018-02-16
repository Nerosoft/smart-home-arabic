var mysql = require('mysql');

var StateConnection=false;

var con = mysql.createConnection({ // بيانات الاتصال بقاعده البيانات
  host: "localhost", // السيرفر
  user: "root", // اسم المستخدم
  port:  "3305", // المنفذ الخاص بالقعده
  password: "", // كلمه المرور
  database: "node_mysql" // اسم القاعده
});

// حاله الاتصال بالقاعده
if(!StateConnection)
  con.connect(function(err) {
    if (err) throw err;
    console.log("Connected!");
    StateConnection=true;
  });






module.exports={
  selectOneUser:function select(email, callback){
    con.query("SELECT * FROM users Where email='"+email+"' And GroupU !=0", function (err, result, fields) {
        if (err) throw err;
        console.log(result);
        callback(result);
      });
  },
  selectOneUserInsert:function select(email, callback){
    con.query("SELECT * FROM users Where email='"+email+"' And GroupU !=4", function (err, result, fields) {
        if (err) throw err;
        console.log(result);
        callback(result);
      });
  },
  insertUser:function(user ,password ,GroupU ,comment,callback){
    var sql = "INSERT INTO users (email, password ,GroupU ,comment) VALUES ('"+user+"', '"+password+"', '"+GroupU+"' , '"+comment+"')";
    con.query(sql, function (err, result) {
      callback(err ,result);
    });
   
  },
  selectAllUser:function(callback){
    con.query("SELECT id,email,GroupU,comment FROM users WHERE GroupU !=4", function (err, result, fields) {
        callback(err, result, fields);
      });
  },
  activeUser:function(id ,callback){//
    var sql = "UPDATE users SET GroupU = 1 WHERE id = '"+id+"'";
    con.query(sql, function (err, result) {
      callback(err, result)   
    });
  },
  deleteUser:function(id ,callback){//
    var sql = "DELETE FROM users WHERE id = '"+id+"'";
    con.query(sql, function (err, result) {
      callback(err, result)   
    });
  },
  updateUser:function(id ,user ,password ,GroupU ,comment ,callback){//
    var sql;
    if(password=="----------")
         sql= "UPDATE users SET email = '"+user+"' ,GroupU = '"+GroupU+"' ,comment = '"+comment+"' WHERE id = '"+id+"'";
    else sql= "UPDATE users SET email = '"+user+"' ,password = '"+password+"' ,GroupU = '"+GroupU+"' ,comment = '"+comment+"' WHERE id = '"+id+"'";
    con.query(sql, function (err, result) {
      callback(err, result)   
    });
  },
  updateAdminUser:function(user ,password  ,callback){//
    var sql= "UPDATE users SET password = '"+password+"' WHERE email = '"+user+"'";
    con.query(sql, function (err, result) {
      callback(err, result)   
    });
  },
  getInfoDash:function(callback){
    var info={state:true,member:"",PandingMember:""};
    var sql_member        = "SELECT COUNT(id) FROM users WHERE GroupU !=4";
    var sql_PandingMember = "SELECT COUNT(id) FROM users WHERE GroupU=0";
    con.query(sql_member, function (err, result) {
      if (err)info.state=false //throw err;
      info.member=result[0]["COUNT(id)"];
      console.log(result.affectedRows + " record(s) sql_member");
      //-----------------------------------------------------------
      con.query(sql_PandingMember, function (err, result) {
        if (err)info.state=false //throw err;
        info.PandingMember=result[0]["COUNT(id)"];
        callback(info);
        console.log(result.affectedRows + " record(s) sql_PandingMember");
      });
    });    
  },
  getLemt10User:function(callback){
    var sql = "SELECT id,email,GroupU,comment FROM users WHERE GroupU !=4 LIMIT 10";
    con.query(sql, function (err, result) {
     callback(err,result);
    });
  },
  setUpdateNameLed:function(id,newname,callback){
    var sql = "UPDATE arduino_led SET name = '"+newname+"' WHERE id = "+id+"";
    con.query(sql, function (err, result) {
      callback(err, result);
    });
  },
  getArduinoLed:function(callback){
    var sql = "SELECT * FROM arduino_led ORDER BY arduino_led.id ASC ";
    con.query(sql, function (err, result) {
      if (err) throw err;
      callback(result);
    });
  },
  getArduinoSinsor:function(callback){
    var sql = "SELECT * FROM setting";
    con.query(sql, function (err, result) {
      if (err) throw err;
      callback(result);
    });
  }

}

function update(){
    var sql = "UPDATE customers SET users = 'Canyon 123' WHERE name = 'Valley 345'";
    con.query(sql, function (err, result) {
      if (err) throw err;
      console.log(result.affectedRows + " record(s) updated");
    });
}


function delet(){
    var sql = "DELETE FROM users WHERE email = 'Mountain 21'";
    con.query(sql, function (err, result) {
      if (err) throw err;
      console.log("Number of records deleted: " + result.affectedRows);
    });
}


function selectlimet(){

    var sql = "SELECT * FROM customers LIMIT 5";
    con.query(sql, function (err, result) {
      if (err) throw err;
      console.log(result);
    });
}
