package nero_soft.com.nero_soft.networkSocket;



import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by root on 26/01/18.
 */

public  class SocketIo {

    public static Socket socket; // تعريف السوكيت
    public static boolean StateForConnectionSocekt; // تعريف متغير يدل علي حالة السوكت
    public static void setUpSocketIo(String IpAdr , String Port){ // تمهيد بيانات السوكيت
        try {
            socket = IO.socket("http://"+IpAdr+":"+Port); // ادخال العنوان والمنفذ لعملية
            StateForConnectionSocekt=true;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            StateForConnectionSocekt=false;
        }
    }

}
