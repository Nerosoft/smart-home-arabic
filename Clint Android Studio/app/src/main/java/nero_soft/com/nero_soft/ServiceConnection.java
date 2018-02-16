package nero_soft.com.nero_soft;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import nero_soft.com.nero_soft.ViewHolder.SmartHome;
import nero_soft.com.nero_soft.animation.MyAinm;
import nero_soft.com.nero_soft.interFace.Setting;
import nero_soft.com.nero_soft.networkSocket.SocketIo;
import nero_soft.com.nero_soft.networkSocket.UserLoginTask;

/**
 * Created by MRNerO on 7/24/2017.
 */

public class ServiceConnection extends Service {

    public static int Lenght = 4;               // رقم البن في المتحكمه الذي سيبدأ اول بن اخراج فولت
    public static JSONArray Led = null;        // تعريف متغير يحتوي علي بينات كل ليد او لمبه او جهاز كهربي
    /*--------------------------------------------------------------Led---------------------------------------------------------
    --(1)id          -> هنا يتم تخذين موقع الليد او الجهاز الكهربي الخاص بالمصفوفه ويبدأ من صفر                                -
    --(2)name        -> يتم تخزين هنا اسم الليد او الجهاز الكهربي                                                              -
    --(3)state       -> ينم تخذين هنا حاله الجهاز اذا كانت تشغيل ام ايقاف                                                      -
    --(4)led         -> يتم تخزين هنا رقم البن الخاص بال المتحكمه                                                              -
    --(5)ledinterval -> يتم تخزين هنا الوقت الخاص بالجهاز الكهربي حيث يتم تطبيق وضع الايقاف اوالتشغيل بمجرد انتهاء الوقت        -
    --(6)refinterval -> يتم حفظ اخر وقت تم وضعه للجهاز الكهربي اذا تم تطبيق تحديث الوقت مره اخري                               -
    --(7)statetime   -> حالة لة الجهاز الكهربي التي سيتم تطبيقها بعد انتهاء الوقت                                              -
    --(8)STL         -> حالة التيمر ويقصد بها التيمر يعمل ام لا بمجرد انتهاء التيمر يتم استبعاده                                -
    --(9)pir         -> هذا الجهاز منضم لمجموعه اجهزه حساس الحركه ام لا حيث يتم تطبيق بيانات الحساس علي الجهاز اذا استقبل حركه  -
    ----------------------------------------------------------------------------------------------------------------------------*/

    public static JSONObject Sensor_State;    // بيانات الحساسات في المتحكمه
    /*--------------------------------------Sensor_State----------------
    --(1)id   -> ؤقم العنوان لم يستغدم                            -
    --(2)SPIR -> الحاله الخاصه بحساس الحركه تشغيل ام ايقاف        -
    --(3)time -> الوقت الخاص بالحساس الحركه                       -
    --(4)mode -> الوضع الخاص بحساس الحركه                         -
    --(5)key  -> تم التعريف من قبل                                -
    --(6)SIR  -> حساس الريموت تشغيل ام ايقاف                      -
    --------------------------------------------------------------*/

    public static JSONObject Dash_Info;      // جلب عدد العملاء وعدد الاتصالات وعدد المستخدمين ال غير نشطين والاجهزه التي تتبع للحساس الحركه
    public static String User;               // اسم المستخدم
    public static String Pass;              //  كلمه المرور
    public static String TypeOfUser;        // نوع العميل
    public static Context Servcontext;
    public static String Ip = "192.168.1.5"; // عنوان السيرفر
    public static String Port = "3000";        //البورت

    public ServiceConnection(Context applicationContext) {
        super();
        Servcontext = applicationContext;
        Log.i("HERE", "100/100 ('_')");
    }

    public ServiceConnection() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        SocketIo.setUpSocketIo(Ip, Port); // بيانات الاتصال بالسيرفر
        this.sendLoginSOCK();              // تسجيل السوكت للعميل
        this.setUpLisinSocektIo();       // تسطيب احداث الاستماع للرساليل من قبل السوكت
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        SocketIo.socket.disconnect(); // تسجيل الغروج من السوكت
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    // رساله لتشغيل لمبه او اطفاء
    public static boolean sendLedMessage(String msg, String led) {

        if (checkNetworkState(Servcontext) && SocketIo.StateForConnectionSocekt)
            SocketIo.socket.emit("clintuser", "{\"email\":\"" + User + "\",\"password\":\"" + Pass +
                    "\",\"typemessage\":\"" + msg + "\",\"led\":\"" + led + ":\"}");
        else return socketErrore();
        return true;
    }

    // رساله لعمل مؤقت للمبه
    public static boolean sendTimeLedMessage(String msg, String id, int ledinterval, String statetime, String STL) {
        if (checkNetworkState(Servcontext) && SocketIo.StateForConnectionSocekt)
            SocketIo.socket.emit("clintuser", "{\"email\":\"" + User + "\",\"password\":\"" + Pass +
                    "\",\"typemessage\":\"" + msg + "\" ,\"id\":\"" +
                    id + "\" ,\"ledinterval\":\"" + ledinterval + "\" ,\"refinterval\":\"" +
                    ledinterval + "\" ,\"statetime\":\"" + statetime + "\",\"STL\":\"" + STL + "\"}");
        else return socketErrore();
        return true;
    }

    //رساله لطرد عميل
    public static boolean sendKikoutUser(String msg, int id, String name) {
        if (checkNetworkState(Servcontext) && SocketIo.StateForConnectionSocekt)
            SocketIo.socket.emit("clintuser", "{\"email\":\"" + User + "\",\"password\":\"" + Pass + "\",\"typemessage\":\"" + msg + "\" ,\"id\":\"" + id + "\",\"name\":\"" + name + "\"}");
        else return socketErrore();
        return true;
    }

    // رساله لضبط حساس الحركه
    public static boolean sendSensorPirMessage(String msg, int time, String mode, String led, String state) { //="send"
        if (checkNetworkState(Servcontext) && SocketIo.StateForConnectionSocekt)
            SocketIo.socket.emit("clintuser",
                    "{\"email\":\"" + User + "\",\"password\":\"" + Pass + "\",\"typemessage\":\"" + msg + "\" ,\"time\":\"" + time + "\",\"mode\":\"" + mode + "\",\"led\":\"" + led + "\",\"state\":\"" + state + "\"}");
        else return socketErrore();
        return true;
    }

    // ايقاف او تشغيل الحساسات
    public static boolean sendSensorLedMessage(String msg, String sensor, String state) {
        if (checkNetworkState(Servcontext) && SocketIo.StateForConnectionSocekt)
            SocketIo.socket.emit("clintuser", "{\"email\":\"" + User + "\",\"password\":\"" + Pass + "\",\"typemessage\":\"" + msg + "\" ,\"sensor\":\"" + sensor + "\",\"state\":\"" + state + "\"}");
        else return socketErrore();
        return true;
    }

    public boolean sendLoginSOCK() { // طلب لتسجيل السوكت الخاصه بجهاز العميل
        if (checkNetworkState(Servcontext) && SocketIo.StateForConnectionSocekt)
            SocketIo.socket.emit("clintuserlogin", "{\"email\":\"" + User + "\"" +
                    ",\"password\":\"" + Pass + "\"}");
        else return socketErrore();
        return true;
    }

    // هنا يتم معالجة جميع الرسايل المستقبله من السيرفر

    private void setUpLisinSocektIo() {

        if (this.checkNetworkState(getBaseContext()) || SocketIo.StateForConnectionSocekt) {
            SocketIo.socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    //her
                    SocketIo.StateForConnectionSocekt = true;
                }

            }).on("message", new Emitter.Listener() { // مفتاح الدخول للحدث

                @Override
                public void call(Object... args) {
                    try {
                        if (TypeOfUser == "admin")
                            setPlusConnection(); // لو الدمن هيزود عدد الاتصالات بواحد

                        JSONObject command = new JSONObject(new JSONObject(String.valueOf(args[0])).getString("msg")); // تحويل النص الي جيسون
                        if (command.getString("msg").contentEquals("ledon") ||
                                command.getString("msg").contentEquals("ledof")) { // for user لو اطفاء او تشغيل ليد

                            final int position = (command.getInt("numb") - Lenght); // جلب موقع الفيو الخاص بالريسيكل
                            final String state = (command.getString("msg").contentEquals("ledon")) ? "on" : "of"; // جلب الحاله الخاصه باليد
                            Led.getJSONObject(position).put("state", state); // ادخال الحاله في المتغير الخاص باليد في الاوبجيكت
                            MainActivity.activity.runOnUiThread(new Runnable() { // الدخول الي الثريد الخاص بالمين اكتفتي
                                @Override
                                public void run() {
                                    // الوصل للفيو الخاص بموقع الليد في الريسايكل
                                    View view_reys = MainActivity.SmartHome.smartHome.recyclerView.getLayoutManager().findViewByPosition(position);
                                    if (view_reys != null) { // لو تم الوصول للفيو
                                        SmartHome.ViewHolder viewHolder = new SmartHome.ViewHolder(view_reys, 1); // تمهيد الفيو
                                        viewHolder.setActionOnAndOff(state); // تطبيق الحاله علي الفيو
                                    } else // لو الفيو لا يحتوي علي قيمه
                                        MainActivity.SmartHome.smartHome.notifyItemChanged(position); // عمل ريفرش للريسايكل
                                }
                            });

                        } else if (command.getString("msg").contentEquals("timeled")) { // for user لو عمل وقت لليد
                            //   Log.i("TIMEMAN", command.toString());
                            if (command.getString("STL").contentEquals("on") || command.getString("STL").contentEquals("of")) {
                                final int position = command.getInt("id");  // جلب موقع الفيو الخاص بالريسيكل
                                final String STL = command.getString("STL"); // جلب خاله التيمر
                                final String ledinterval = command.getString("ledinterval"); // جلب وقت التيمر
                                final String statetime = command.getString("statetime"); // جلب نوع التيمر ايقاف ام تشغيل
                                Led.getJSONObject(position).put("ledinterval", ledinterval); // حفظ وقت التيمر
                                Led.getJSONObject(position).put("statetime", statetime); // حفظ نوع التيمر
                                Led.getJSONObject(position).put("STL", STL); // حفظ حالة التيمر
                                final String text_info = (STL.contentEquals("on")) // عرض حاله الوقت والنوع علي النص
                                        ? "Time Is " + ledinterval + " M " + statetime : Led.getJSONObject(position).getString("name");
                                MainActivity.activity.runOnUiThread(new Runnable() { // الدخول الي الثريد الخاص بالمين اكتفتي
                                    @Override
                                    public void run() {
                                        // الوصل للفيو الخاص بموقع الليد في الريسايكل
                                        View view_reys = MainActivity.SmartHome.smartHome.recyclerView.getLayoutManager().findViewByPosition(position);
                                        if (view_reys != null) { // لو تم الوصول للفيو
                                            SmartHome.ViewHolder viewHolder = new SmartHome.ViewHolder(view_reys, 2); // تمهيد الفيو
                                            viewHolder.setActionTimeOnAndOff(STL, text_info);
                                        } else  // لو الفيو لا يحتوي علي قيمه
                                            MainActivity.SmartHome.smartHome.notifyItemChanged(position);

                                    }
                                });
                            } else if (command.getString("STL").contentEquals("ofall")) { // for user لو اطفاء جميع التيمر

                                for (int i = 0; i < Led.length(); i++) { // الوصول لمحتويات الليد
                                    JSONObject element = Led.getJSONObject(i); // جلب اوبجيكت
                                    if (element.getString("STL").contentEquals("on")) { // جلب نوع التيمر داخل الاوبجيكت
                                        final int position = i;// جلب الموقع الخاص بالاوبجيكت داخل الاري الخاص باليد
                                        final String text_info = element.getString("name");// اعادة وضع اسم الليد علي النص
                                        Led.getJSONObject(i).put("STL", "of");// وضع حالة التيمر
                                        MainActivity.activity.runOnUiThread(new Runnable() { // الدخول الي الثريد الخاص بالمين اكتفتي
                                            @Override
                                            public void run() {
                                                // الوصل للفيو الخاص بموقع الليد في الريسايكل
                                                View view_reys = MainActivity.SmartHome.smartHome.recyclerView.getLayoutManager().findViewByPosition(position);
                                                if (view_reys != null) {// لو تم الوصول للفيو
                                                    SmartHome.ViewHolder viewHolder = new SmartHome.ViewHolder(view_reys, 2); // تمهيد الفيو
                                                    viewHolder.setActionTimeOnAndOff("of", text_info);
                                                } else // لو الفيو لا يحتوي علي قيمه
                                                    MainActivity.SmartHome.smartHome.notifyItemChanged(position);
                                            }
                                        });
                                    }
                                }

                            }
                        } else if (command.getString("msg").contentEquals("ledallrun") ||
                                command.getString("msg").contentEquals("ledallstop")) { // for user اطفاء او تشغيل جميع الاجهزه الكهربيه
                            // Log.i("ledallrun ", command.toString());
                            final String state = (command.getString("msg").contentEquals("ledallrun")) ? "on" : "of"; // جلب حاله الليد
                            for (int i = 0; i < Led.length(); i++) // // الوصول لمحتويات الليد
                                if (!Led.getJSONObject(i).getString("state").contentEquals(state)) { // جلب حالة الليد
                                    final int position = i;// جلب الموقع الخاص بالاوبجيكت داخل الاري الخاص باليد
                                    Led.getJSONObject(position).put("state", state);// حفظ الحاله الديده
                                    MainActivity.activity.runOnUiThread(new Runnable() { // الدخول الي الثريد الخاص بالمين اكتفتي
                                        @Override
                                        public void run() {
                                            // الوصل للفيو الخاص بموقع الليد في الريسايكل
                                            View view_reys = MainActivity.SmartHome.smartHome.recyclerView.getLayoutManager().findViewByPosition(position);
                                            if (view_reys != null) {// لو تم الوصول للفيو
                                                SmartHome.ViewHolder viewHolder = new SmartHome.ViewHolder(view_reys, 1); // تمهيد الفيو
                                                viewHolder.setActionOnAndOff(state);
                                            } else // لو الفيو لا يحتوي علي قيمه
                                                MainActivity.SmartHome.smartHome.notifyItemChanged(position);
                                        }
                                    });
                                }

                            // {"id":0,"SPIR":1,"time":"50","mode":"2","led":"","SIR":1,"key":3}
                        } else if (command.getString("msg").contentEquals("lediron") ||
                                command.getString("msg").contentEquals("ledirstop")) { // for user تعديل بيانات حساس الريموت
                            Sensor_State.put("SIR", command.getString("msg").contentEquals("lediron") ? 1 : 0); // وضع الحاله الجديده

                            MainActivity.activity.runOnUiThread(new Runnable() { // الدخول الي الثريد الخاص بالمين اكتفتي
                                @Override
                                public void run() {
                                    SIR(); // تعديل الوضع في النافيجيشن
                                }
                            });
                            //    Log.i("Sensor_State ", Sensor_State.toString() + "\n");
                        } else if (command.getString("msg").contentEquals("ledpiron") ||
                                command.getString("msg").contentEquals("ledpirstop")) { // for user تعديل حساس الحركه
                            Sensor_State.put("SPIR", command.getString("msg").contentEquals("ledpiron") ? 1 : 0); //وضع الحاله الجديده
                            MainActivity.activity.runOnUiThread(new Runnable() {// الدخول الي الثريد الخاص بالمين اكتفتي
                                @Override
                                public void run() {
                                    SPIR(); // تعديل الوضع في النافيجيشن
                                }
                            });

                        }
                        //-----------------------------ADMIN MAN ----------------------------------- للادمن فقط

                        if (TypeOfUser == "admin") {
                            if (command.getString("msg").contentEquals("kik")) { //for admin تيليغ بأنه تم طرد عميل
                                if (MainActivity.DashBoard.dashBoard.MAD.Position == 1) { // لو الليست الخاصه بالاكتف التي تظهر
                                    final int Pos = command.getInt("id"); // جلب الموقع الخاص بالعميل
                                    MainActivity.activity.runOnUiThread(new Runnable() { // الدخول للثريد المين
                                        @Override
                                        public void run() {
                                            // جلب الفيو في الليست فيو
                                            final View view = MainActivity.DashBoard.dashBoard.getViewByPosition(Pos);
                                            MyAinm.fade(new MyAinm.ClickAction() { // عمل تأثير الخفاء
                                                @Override
                                                public void click() {

                                                    try {
                                                        JSONArray jsonArray = ServiceConnection.Dash_Info.getJSONArray("useractive"); // جلب مصفوفه العملاء الاكتف
                                                        JSONArray sds = MainActivity.DashBoard.dashBoard.removeItemJson(jsonArray, Pos); // مسح العميل من المصفوفه
                                                        ServiceConnection.Dash_Info.put("useractive", sds); // تحديث المصفوفه

                                                        MainActivity.DashBoard.dashBoard.array.remove(Pos); // مسح العميل من الاري الخاص بالاكتف
                                                        MainActivity.DashBoard.dashBoard.MAD.notifyDataSetChanged(); // تحديث الليست فيو
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    MainActivity.DashBoard.dashBoard.setEditeDashInfo("User Active", "-"); // انقاص عداد الاكتف بواحد
                                                }
                                            }, view, 500, 1.0f, 0.0f);
                                        }
                                    });

                                }
                            } else if (command.getString("msg").contentEquals("option_pir")) { // for admin استقبال بيانات حساس الحركه
                                ServiceConnection.Sensor_State.put("time", command.getString("time"));  // وضع الوقت الجديد
                                ServiceConnection.Sensor_State.put("mode", command.getString("mode")); // وضع المود الجديد للحساس
                                String strArray[] = command.getString("led").split(",");  // جلب الاجهزه التي تتبع للحساس

                                for (int i = 0; i < Led.length(); i++) // الوصول للاري الخاصه بالليد
                                    Led.getJSONObject(i).put("pir", (strArray[i].contains("1")) ? 1 : 0); // وضع حالة الجهاز بنالنسبه للحساس الحركه
                                MainActivity.activity.runOnUiThread(new Runnable() {// الدخول  الي ثريد المين
                                    @Override
                                    public void run() {
                                        MainActivity.DashBoard.dashBoard.sinsor.setInfoPir(); // اعادة ضبط معلومات حساس الحركه
                                        if (MainActivity.DashBoard.dashBoard.MAD.Position == 4) { // لو قائمة الليد الخاصه بحساس الحركه ظاهره امام المستخدم
                                            MainActivity.DashBoard.dashBoard.setArrayLedState();  // اعادة ضبط معلومات الاجهزه التي تتبع لحساس الحركه
                                            MainActivity.DashBoard.dashBoard.MAD.notifyDataSetChanged(); // عمل ريفريش للليست فيو
                                        }
                                    }
                                });

                            }
                        }//End If For Admin
                        else {
                            if (command.getString("msg").contentEquals("kik")) { // حدث طرد العميل العادي وليس الادمن
                                if (command.getString("name").contentEquals(User)) { // لو العميل يتابق اسمه هذا الاسم
                                    MainActivity.activity.finish(); // تسجيل خروج
                                    onDestroy();  // تحطيم السيرفيس
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("EwwwwwwwqwqRRR", "s-_-s");
                    }
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() { // حدث قطع الاتصال

                @Override
                public void call(Object... args) {
                    SocketIo.StateForConnectionSocekt = false;
                }

            });
            SocketIo.socket.connect(); // الاتصال بالسيرفر
        }
    }

    // مضاعفة عداد الاتصلات للسوكيت
    private void setPlusConnection() {
        MainActivity.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity.DashBoard.dashBoard.setEditeDashInfo("connection", "+");
            }
        });
    }
    // هنا لو انا عايز اظهر رسايل
    public static void MeassagDialog(String s, View view, View.OnClickListener clickListener) {

        Snackbar.make(view, s, Snackbar.LENGTH_LONG)
                .setAction("OK", clickListener).show();
    }
    // تحديث حساس الحركه في النافيجيشن
    public static void SPIR() {
        try {
            if (Sensor_State.getString("SPIR").contains("true") ||
                    Sensor_State.getString("SPIR").contains("1")) {
                MainActivity.navigationView.getMenu().findItem(R.id.nav_PIRON).setChecked(true);
                MainActivity.navigationView.getMenu().findItem(R.id.nav_PIROF).setChecked(false);
            } else {
                MainActivity.navigationView.getMenu().findItem(R.id.nav_PIRON).setChecked(false);
                MainActivity.navigationView.getMenu().findItem(R.id.nav_PIROF).setChecked(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    // تحديث حساس الريموت في النافيجيشن
    public static void SIR() {
        try {
            if (Sensor_State.getString("SIR").contains("true") ||
                    Sensor_State.getString("SIR").contains("1")) {
                MainActivity.navigationView.getMenu().findItem(R.id.nav_IRON).setChecked(true);
                MainActivity.navigationView.getMenu().findItem(R.id.nav_IROF).setChecked(false);
            } else {
                MainActivity.navigationView.getMenu().findItem(R.id.nav_IRON).setChecked(false);
                MainActivity.navigationView.getMenu().findItem(R.id.nav_IROF).setChecked(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    // جلب بيانات الليد
    public static void getSettingLedInfo(final Setting.GetPost.getSettingLedInfo getSettingLedInfo) {
        JSONObject Send_Data = new JSONObject();
        try {
            Send_Data.put("email", User);
            Send_Data.put("password", Pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final UserLoginTask SettingLedInfo = new UserLoginTask("settingLed", Send_Data, new Setting.SetPost() {
            @Override
            public void PostExecute(JSONObject result) {

                getSettingLedInfo.PostExecuteSettingLedInfo(result);

            }

            @Override
            public void Cansle() {

            }
        });
        SettingLedInfo.execute((Void) null);
    }
    // جلب بيانات الداش بورد خاص بالادمن فقط
    public static void getSettingDashInfo(final Setting.GetPost.getSettingLedInfo getSettingLedInfo) {
        JSONObject Send_Data = new JSONObject();
        try {
            Send_Data.put("email", User);
            Send_Data.put("password", Pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final UserLoginTask SettingLedInfo = new UserLoginTask("mopgetalldash", Send_Data, new Setting.SetPost() {
            @Override
            public void PostExecute(JSONObject result) {

                getSettingLedInfo.PostExecuteSettingLedInfo(result);

            }

            @Override
            public void Cansle() {

            }
        });
        SettingLedInfo.execute((Void) null);
    }
    // تعديل بيانات الاعضاء
    public static void setEditeMemberInfo(final Setting.GetPost.getSettingLedInfo getSettingLedInfo, JSONObject Send_Data) {
        try {
            Send_Data.put("email", User);
            Send_Data.put("password", Pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final UserLoginTask SettingLedInfo = new UserLoginTask("member", Send_Data, new Setting.SetPost() {
            @Override
            public void PostExecute(JSONObject result) {

                getSettingLedInfo.PostExecuteSettingLedInfo(result);

            }

            @Override
            public void Cansle() {

            }
        });
        SettingLedInfo.execute((Void) null);
    }
    // انشاء عميل جديد
    public static void setCreateMemberInfo(final Setting.GetPost.getSettingLedInfo getSettingLedInfo, JSONObject Send_Data) {
        try {
            Send_Data.put("email", User);
            Send_Data.put("password", Pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final UserLoginTask SettingLedInfo = new UserLoginTask("regg", Send_Data, new Setting.SetPost() {
            @Override
            public void PostExecute(JSONObject result) {

                getSettingLedInfo.PostExecuteSettingLedInfo(result);

            }

            @Override
            public void Cansle() {

            }
        });
        SettingLedInfo.execute((Void) null);
    }
    // تعديل اسم الليد
    public static void setChangeLedName(final Setting.GetPost.getSettingLedInfo getSettingLedInfo, JSONObject Send_Data) {
        try {
            Send_Data.put("email", User);
            Send_Data.put("password", Pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final UserLoginTask SettingLedInfo = new UserLoginTask("updateledname", Send_Data, new Setting.SetPost() {
            @Override
            public void PostExecute(JSONObject result) {

                getSettingLedInfo.PostExecuteSettingLedInfo(result);

            }

            @Override
            public void Cansle() {

            }
        });
        SettingLedInfo.execute((Void) null);
    }
    // تسجيل خروج من السيرفر
    public static void logOut(final Setting.GetPost.getSettingLedInfo getSettingLedInfo, JSONObject Send_Data) {
        try {
            Send_Data.put("email", User);
            Send_Data.put("password", Pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final UserLoginTask SettingLedInfo = new UserLoginTask("moplogout", Send_Data, new Setting.SetPost() {
            @Override
            public void PostExecute(JSONObject result) {

                getSettingLedInfo.PostExecuteSettingLedInfo(result);

            }

            @Override
            public void Cansle() {

            }
        });
        SettingLedInfo.execute((Void) null);
    }
    // هنا بتأكد ان الوير لس شغال
    public static boolean checkNetworkState(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
    // اظهار رسالة ايروري
    public static boolean socketErrore() {
        MeassagDialog("Please Check Your Connection And Try Again", MainActivity.relativeLayout, null);
        return false;
    }

}


