package nero_soft.com.nero_soft.networkSocket;

import android.os.AsyncTask;
import android.util.Log;

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

import nero_soft.com.nero_soft.ServiceConnection;
import nero_soft.com.nero_soft.interFace.Setting;

/**
 * Created by nero on 12/02/18.
 */

public class UserLoginTask  extends AsyncTask<Void, Void, Boolean> {

    private final String Rout;
    private final String post_url;
    private JSONObject Json_Send;
    private String data;
    private final Setting.SetPost CallBack;

    public UserLoginTask(String rout, JSONObject json_send, Setting.SetPost callBack) {
        Rout = rout;
        Json_Send = json_send;
        CallBack = callBack;
        post_url = "http://" + ServiceConnection.Ip + ":" + ServiceConnection.Port + "/" + Rout;
//            progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        // TODO: attempt authentication against a network service.
        try {
            Json_Send.put("wtype", "MOP");
            URL url = new URL(post_url);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            OutputStream os = urlConnection.getOutputStream();
            os.write(Json_Send.toString().getBytes("UTF-8"));
            os.close();
            BufferedInputStream input_stream = new BufferedInputStream(urlConnection.getInputStream());
            data = this.getSTR(input_stream);
            Log.i("sms", data);
            input_stream.close();

            urlConnection.connect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }


        // TODO: register the new account here.
        return true;
    }

    String getSTR(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String text = "";
        try {
            while ((line = reader.readLine()) != null) {
                text += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //  Log.i("getSTR", text);
        return text;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        JSONObject jsonObject;
        if (success) {
            try {
                jsonObject = new JSONObject(data);
                CallBack.PostExecute(jsonObject);

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    jsonObject = new JSONObject();
                    jsonObject.put("state", false);
                    ServiceConnection.socketErrore();
                    CallBack.PostExecute(jsonObject);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

        } else {
            try {
                jsonObject = new JSONObject();
                jsonObject.put("connaction", false);
                CallBack.PostExecute(jsonObject);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onCancelled() {
        CallBack.Cansle();
    }
}