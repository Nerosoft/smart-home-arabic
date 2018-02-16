package nero_soft.com.nero_soft.ViewHolder;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import android.support.v4.app.FragmentManager;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import nero_soft.com.nero_soft.DialogClass.EditeDialogUser;
import nero_soft.com.nero_soft.MainActivity;
import nero_soft.com.nero_soft.R;
import nero_soft.com.nero_soft.ServiceConnection;
import nero_soft.com.nero_soft.animation.MyAinm;
import nero_soft.com.nero_soft.interFace.Setting;

/**
 * Created by root on 21/01/18.
 */

public class DashBoard implements WaveSwipeRefreshLayout.OnRefreshListener {
  private android.app.FragmentManager manager;
    private LayoutInflater Inflater;
    private ViewGroup Container;
    public static LinearLayout linearLayout;

    public ArrayList<ItemDash> array;
    public MySimpleArrayAdapter MAD;
    public PirSinsor sinsor;
    public FrameLayout FragDAsh;
    public ListView listViewLid;
    public LinearLayout LaySinsorPirSetting;

    public ImageView ButDashUsersActive;
    public ImageView ButDashUsers;
    public ImageView ButDashUsersWait;
    public ImageView ButDashSinsorPir;
    public ImageView ButDashAllLedSinsor;

    public ProgressBar prog_user_dash, prog_user_wait_dash, prog_user_active_dash, prog_connection_dash;
    public TextView text_user_dahs, text_user_wait_dash, text_user_active_dash, text_connection_dash;

    WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;

    public DashBoard( android.app.FragmentManager manager, LayoutInflater inflater, ViewGroup container) {
        this.Inflater = inflater;
        this.Container = container;
        this.manager=manager;
    }


    public void setUpDashBoardView() { //-------------------------------set up contanir RecyclerView adaptor
        this.linearLayout = (LinearLayout) this.Inflater.inflate(R.layout.lay_dahs_board, this.Container, false);
        array = new ArrayList();

        this.ButDashUsersActive = this.linearLayout.findViewById(R.id.but_dash_users_active);
        this.ButDashUsers = this.linearLayout.findViewById(R.id.but_dash_users);
        this.ButDashUsersWait = this.linearLayout.findViewById(R.id.but_dash_users_wait);
        this.ButDashSinsorPir = this.linearLayout.findViewById(R.id.but_dash_sinsor_pir);
        this.ButDashAllLedSinsor = this.linearLayout.findViewById(R.id.but_dash_all_led_sinsor);

        this.FragDAsh = this.linearLayout.findViewById(R.id.frag_dash);
        this.listViewLid = this.linearLayout.findViewById(R.id.list_dahs);
        this.LaySinsorPirSetting = this.linearLayout.findViewById(R.id.lay_sinsor_pir_setting);

//-------------------------------------------------------------------------------------------up info

        prog_user_dash = this.linearLayout.findViewById(R.id.prog_user_dash);
        prog_user_wait_dash = this.linearLayout.findViewById(R.id.prog_user_wait_dash);
        prog_user_active_dash = this.linearLayout.findViewById(R.id.prog_user_active_dash);
        prog_connection_dash = this.linearLayout.findViewById(R.id.prog_connection_dash);

//------------------------------------------------------------------------------------------------
        text_user_dahs = this.linearLayout.findViewById(R.id.text_user_dahs);
        text_user_wait_dash = this.linearLayout.findViewById(R.id.text_user_wait_dash);
        text_user_active_dash = this.linearLayout.findViewById(R.id.text_user_active_dash);
        text_connection_dash = this.linearLayout.findViewById(R.id.text_connection_dash);


        mWaveSwipeRefreshLayout = this.linearLayout.findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setWaveColor(Color.argb(100, 255, 0, 0));
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);

    }

    @Override
    public void onRefresh() {
        // Do work to refresh the list here.
        ServiceConnection.getSettingDashInfo(new Setting.GetPost.getSettingLedInfo() {
            @Override
            public void PostExecuteSettingLedInfo(JSONObject result) {
                try {
                    if (result.getString("state").contentEquals("true")) {
                        ServiceConnection.Dash_Info = result;
                        ServiceConnection.Sensor_State = result.getJSONArray("Setting").getJSONObject(0);
                        for (int i = 0; i < ServiceConnection.Led.length(); i++) {
                            ServiceConnection.Led.getJSONObject(i).put("pir",
                                    result.getJSONArray("pir").getJSONObject(i).getInt("pir"));
                        }
                        setUpInfoDash();
                        MyAinm AnmiRotat = new MyAinm();
                        switch (MAD.Position) {

                            case 1:
                                MyAinm.setRotationWithVisiblity(new MyAinm.ClickAction() {
                                                                    @Override
                                                                    public void click() {
                                                                        MAD = new MySimpleArrayAdapter(Container.getContext(), Inflater,
                                                                                jsonToArray("useractive", "KIK OUT", "all"), 1);
                                                                        listViewLid.setAdapter(MAD);

                                                                    }
                                                                }, FragDAsh, AnmiRotat.Alpha, 500, 200,
                                        LaySinsorPirSetting, listViewLid, 1f, 0.0f, 0.0f, 1f);
                                break;

                            case 2:
                                MyAinm.setRotationWithVisiblity(new MyAinm.ClickAction() {
                                                                    @Override
                                                                    public void click() {
                                                                        if (LaySinsorPirSetting.getVisibility() == View.VISIBLE)
                                                                            LaySinsorPirSetting.setVisibility(View.GONE);
                                                                        MAD = new MySimpleArrayAdapter(Container.getContext(), Inflater,
                                                                                jsonToArray("users", "EDIT", "all"), 2);
                                                                        listViewLid.setAdapter(MAD);
                                                                    }
                                                                }, FragDAsh, AnmiRotat.Alpha, 500, 200,
                                        listViewLid, listViewLid, 1f, 0.0f, 0.0f, 1f);
                                break;

                            case 3 :
                                MyAinm.setRotationWithVisiblity(new MyAinm.ClickAction() {
                                                                    @Override
                                                                    public void click() {
                                                                        if (LaySinsorPirSetting.getVisibility() == View.VISIBLE)
                                                                            LaySinsorPirSetting.setVisibility(View.GONE);
                                                                        MAD = new MySimpleArrayAdapter(Container.getContext(), Inflater,
                                                                                jsonToArray("users", "ACTIVE", "Wait"), 3);
                                                                        listViewLid.setAdapter(MAD);
                                                                    }
                                                                }, FragDAsh, AnmiRotat.Alpha, 500, 200,
                                        listViewLid, listViewLid, 1f, 0.0f, 0.0f, 1f);

                                break;

                            case 5:

                                MyAinm.setRotationWithVisiblity(new MyAinm.ClickAction() {
                                                                    @Override
                                                                    public void click() {
                                                                        MAD.Position = 5;
                                                                        sinsor.setInfoPir();
                                                                    }
                                                                }, FragDAsh, AnmiRotat.Alpha, 500, 200,
                                        listViewLid, LaySinsorPirSetting, 1f, 0.0f, 0.0f, 1f);

                                break;

                            case 4:
                                MyAinm.setRotationWithVisiblity(new MyAinm.ClickAction() {
                                                                    @Override
                                                                    public void click() {
                                                                        if (LaySinsorPirSetting.getVisibility() == View.VISIBLE)
                                                                            LaySinsorPirSetting.setVisibility(View.GONE);


                                                                        MAD = new MySimpleArrayAdapter(Container.getContext(), Inflater, setArrayLedState(), 4);
                                                                        listViewLid.setAdapter(MAD);
                                                                    }
                                                                }, FragDAsh, AnmiRotat.Alpha, 500, 200,
                                        listViewLid, listViewLid, 1f, 0.0f, 0.0f, 1f);

                                break;

                        }

                        mWaveSwipeRefreshLayout.setRefreshing(false);
                    }
                    mWaveSwipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    mWaveSwipeRefreshLayout.setRefreshing(false);
                    e.printStackTrace();
                }

            }
        });
    }

    class Tasks extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... voids) {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new String[0];
        }

        @Override
        protected void onPostExecute(String[] result) {
            // Call setRefreshing(false) when the list has been refreshed.
            mWaveSwipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(result);
        }
    }

    public void setUpButtonDashBord() {
        final MyAinm AnmiRotat = new MyAinm();
        this.ButDashUsersActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MAD.Position != 1) {
                    MyAinm.setRotationWithVisiblity(new MyAinm.ClickAction() {
                                                        @Override
                                                        public void click() {

                                                            MAD = new MySimpleArrayAdapter(Container.getContext(), Inflater,
                                                                    jsonToArray("useractive", "KIK OUT", "all"), 1);
                                                            listViewLid.setAdapter(MAD);

                                                        }
                                                    }, FragDAsh, AnmiRotat.Alpha, 500, 200,
                            LaySinsorPirSetting, listViewLid, 1f, 0.0f, 0.0f, 1f);
                }


            }
        });

        this.ButDashUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (MAD.Position != 2) {
                    MyAinm.setRotationWithVisiblity(new MyAinm.ClickAction() {
                                                        @Override
                                                        public void click() {
                                                            if (LaySinsorPirSetting.getVisibility() == View.VISIBLE)
                                                                LaySinsorPirSetting.setVisibility(View.GONE);
                                                            MAD = new MySimpleArrayAdapter(Container.getContext(), Inflater,
                                                                    jsonToArray("users", "EDIT", "all"), 2);
                                                            listViewLid.setAdapter(MAD);
                                                        }
                                                    }, FragDAsh, AnmiRotat.Alpha, 500, 200,
                            listViewLid, listViewLid, 1f, 0.0f, 0.0f, 1f);
                }

            }
        });

        this.ButDashUsersWait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (MAD.Position != 3) {
                    MyAinm.setRotationWithVisiblity(new MyAinm.ClickAction() {
                                                        @Override
                                                        public void click() {
                                                            if (LaySinsorPirSetting.getVisibility() == View.VISIBLE)
                                                                LaySinsorPirSetting.setVisibility(View.GONE);
                                                            MAD = new MySimpleArrayAdapter(Container.getContext(), Inflater,
                                                                    jsonToArray("users", "ACTIVE", "Wait"), 3);
                                                            listViewLid.setAdapter(MAD);
                                                        }
                                                    }, FragDAsh, AnmiRotat.Alpha, 500, 200,
                            listViewLid, listViewLid, 1f, 0.0f, 0.0f, 1f);
                }

            }
        });

        this.ButDashSinsorPir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (LaySinsorPirSetting.getVisibility() == View.GONE)
                    MyAinm.setRotationWithVisiblity(new MyAinm.ClickAction() {
                                                        @Override
                                                        public void click() {
                                                            MAD.Position = 5;
                                                        }
                                                    }, FragDAsh, AnmiRotat.Alpha, 500, 200,
                            listViewLid, LaySinsorPirSetting, 1f, 0.0f, 0.0f, 1f);
            }
        });

        this.ButDashAllLedSinsor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (MAD.Position != 4) {
                    MyAinm.setRotationWithVisiblity(new MyAinm.ClickAction() {
                                                        @Override
                                                        public void click() {
                                                            if (LaySinsorPirSetting.getVisibility() == View.VISIBLE)
                                                                LaySinsorPirSetting.setVisibility(View.GONE);


                                                            MAD = new MySimpleArrayAdapter(Container.getContext(), Inflater, setArrayLedState(), 4);
                                                            listViewLid.setAdapter(MAD);
                                                        }
                                                    }, FragDAsh, AnmiRotat.Alpha, 500, 200,
                            listViewLid, listViewLid, 1f, 0.0f, 0.0f, 1f);
                }
            }
        });

    }

    public void setUpPirSinsor() {
        this.sinsor = new PirSinsor();
    }

    public void setUpInfoDash() {

        try {
            JSONObject info = ServiceConnection.Dash_Info.getJSONObject("info");
            JSONObject cont = info.getJSONObject("cont");

            prog_user_dash.setProgress(cont.getInt("member") * 5);
            text_user_dahs.setText(cont.getString("member"));

            prog_user_wait_dash.setProgress(cont.getInt("PandingMember") * 5);
            text_user_wait_dash.setText(cont.getString("PandingMember"));

            prog_user_active_dash.setProgress(info.getInt("member") * 5);
            text_user_active_dash.setText(info.getString("member"));

            prog_connection_dash.setProgress(info.getInt("req"));
            text_connection_dash.setText(info.getString("req"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void setUpComponant() {

        MAD = new MySimpleArrayAdapter(this.Container.getContext(), this.Inflater,
                jsonToArray("useractive", "KIK OUT", "all"), 1);
        listViewLid.setAdapter(MAD);
    }

    public ArrayList jsonToArray(String key_json, String state, String type) {
        try {
            array = new ArrayList();
            JSONArray User = ServiceConnection.Dash_Info.getJSONArray(key_json);
            if (User.length() < 1) return array;
            //ItemDash(int id ,String name ,int GroupU ,String comment ,String state)
            if (type == "all")
                for (int i = 0; i < User.length(); i++)
                    array.add(new ItemDash(User.getJSONObject(i).getInt("id"),
                            User.getJSONObject(i).getString("email"),
                            User.getJSONObject(i).getInt("GroupU"),
                            User.getJSONObject(i).getString("comment"), state));
            else
                for (int i = 0; i < User.length(); i++)
                    if (User.getJSONObject(i).getInt("GroupU") != 1)
                        array.add(new ItemDash(User.getJSONObject(i).getString("email"),
                                User.getJSONObject(i).getInt("id"), state));
            return this.array;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this.array;
    }

    public ArrayList setArrayLedState() {
        array = new ArrayList();
        try {

            for (int index = 0; index < ServiceConnection.Led.length(); index++) {
                JSONObject jsonObject = ServiceConnection.Led.getJSONObject(index);
                array.add(new ItemDash(jsonObject.getString("name"),
                        jsonObject.getString("pir")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }

    public View getView() {
        return this.linearLayout;
    }


    public static class ItemDash {
        public String Name;
        public int GroupU;
        public String comment;
        public String State;
        public int Id;

        public ItemDash(String name, int id, String state) {
            this.Name = name;
            this.Id = id;
            this.State = state;
        }

        public ItemDash(String name, String state) {
            this.Name = name;
            this.State = state;
        }

        public ItemDash(int id, String name, int GroupU, String comment, String state) {
            this.Id = id;
            this.Name = name;
            this.GroupU = GroupU;
            this.comment = comment;
            this.State = state;
        }
    }

    public class MySimpleArrayAdapter extends ArrayAdapter<ItemDash> {
        private final Context context;
        LayoutInflater Inflater;
        //  ArrayList<ItemDash> ItemDash;
        public int Position;

        public MySimpleArrayAdapter(Context context, LayoutInflater inflater, ArrayList<ItemDash> item_dash, int position) {
            super(context, R.layout.lay_list_dash, item_dash);
            this.context = context;
            Inflater = inflater;
            // this.ItemDash = item_dash;
            this.Position = position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            switch (this.Position) {
                case 1:
//                    this.ItemDash.get(position).Id=position;
                    UserActive UserActiv = new UserActive(convertView, Inflater, parent, array.get(position), position);
                    return UserActiv.getView();
                case 2:
                    UserAll Users
                            = new UserAll(convertView, Inflater, parent, array.get(position), position);
                    return Users.getView();
                case 3:
                    UserWait UserWait = new UserWait(convertView, Inflater, parent, array.get(position), position);


                    return UserWait.getView();
                case 4:


                    LedPir LedPir = new LedPir(convertView, Inflater, parent, array.get(position), position);
                    LedPir.setUpLedPirList();
                    return LedPir.getView();
                default:
                    return null;
            }

        }


    }

    public class User {

        View VUser;
        int IdImg;
        TextView TextUser;
        ImageView ImgUser;
        CheckBox CheckBoxLed;
        Button ButUser;
        ItemDash ItemDash;
        int Position;

        public User(View convertView, LayoutInflater inflater, ViewGroup parent, ItemDash itemDash, int img, int position) {
            this.VUser = inflater.inflate(R.layout.lay_list_dash, parent, false);
            this.ItemDash = itemDash;
            this.IdImg = img;
            this.Position = position;
            this.setUp();
        }

        public void setUp() {
            this.ImgUser = this.VUser.findViewById(R.id.img_list_dash);
            this.TextUser = this.VUser.findViewById(R.id.text_list_dash);
            this.ButUser = this.VUser.findViewById(R.id.but_list_dash);
            this.CheckBoxLed = this.VUser.findViewById(R.id.check_list_dash);
            this.ImgUser.setImageResource(this.IdImg);
            this.TextUser.setText(this.ItemDash.Name);
            this.ButUser.setText(this.ItemDash.State);
        }


        public View getView() {
            return this.VUser;
        }


    }

    public class UserActive extends User implements View.OnClickListener {
        public UserActive(View convertView, LayoutInflater inflater, ViewGroup parent, ItemDash itemDash, int position) {
            super(convertView, inflater, parent, itemDash, R.drawable.ic_pnal_user_active, position);
            this.ButUser.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            ServiceConnection.MeassagDialog("Do you Want Kik" + ItemDash.Name, DashBoard.linearLayout, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ServiceConnection.sendKikoutUser("kik", Position, ItemDash.Name);
                }
            });

        }
    }

    public class UserAll extends User implements View.OnClickListener {
        public UserAll(View convertView, LayoutInflater inflater, ViewGroup parent, ItemDash itemDash, int position) {
            super(convertView, inflater, parent, itemDash, R.drawable.ic_pnal_user_total, position);
            this.ButUser.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            new EditeDialogUser().EditUser("EDITE", ItemDash, Position).show( manager , "");
        }
    }

    public class UserWait extends User implements View.OnClickListener {
        public UserWait(View convertView, LayoutInflater inflater, ViewGroup parent, ItemDash itemDash, int position) {
            super(convertView, inflater, parent, itemDash, R.drawable.ic_pnal_user_wait, position);
            this.ButUser.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            ServiceConnection.MeassagDialog("Do You Want To Active " + ItemDash.Name, DashBoard.linearLayout, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("id", ItemDash.Id);
                        jsonObject.put("optin", "active");
                        ServiceConnection.setEditeMemberInfo(new Setting.GetPost.getSettingLedInfo() {
                            @Override
                            public void PostExecuteSettingLedInfo(JSONObject result) {
                                try {
                                    if (result.getBoolean("state")) {
                                        JSONArray users = ServiceConnection.Dash_Info.getJSONArray("users");
                                        for (int i = 0; i < users.length(); i++)
                                            if (users.getJSONObject(i).getInt("id") == ItemDash.Id){
                                                ServiceConnection.Dash_Info.getJSONArray("users").getJSONObject(i)
                                                        .put("GroupU", "1");
                                                break;
                                        }
                                        array.remove(Position);
                                        final View view = MainActivity.DashBoard.dashBoard.getViewByPosition(Position);
                                        MyAinm.fade(new MyAinm.ClickAction() {
                                            @Override
                                            public void click() {
                                                setEditeDashInfo("User Wait","-");
                                                MAD.notifyDataSetChanged();
                                            }
                                        }, view, 500, 1.0f, 0.0f);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    public class LedPir extends User implements View.OnClickListener {
        public LedPir(View convertView, LayoutInflater inflater, ViewGroup parent, ItemDash itemDash, int position) {
            super(convertView, inflater, parent, itemDash, R.drawable.ic_ledmotion_sensor, position);
            this.ButUser.setVisibility(View.GONE);
            this.CheckBoxLed.setVisibility(View.VISIBLE);
            this.CheckBoxLed.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //  boolean new_state = Boolean.parseBoolean(array.get(Position).State);
            CheckBox new_state = (CheckBox) view;
            try {
                if (!new_state.isChecked()) {

                    ServiceConnection.Led.getJSONObject(Position).put("pir", 0);
                    array.get(Position).State = "0";

                } else {

                    ServiceConnection.Led.getJSONObject(Position).put("pir", 1);
                    array.get(Position).State = "1";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void setUpLedPirList() {
            this.CheckBoxLed.setChecked(this.ItemDash.State.contains("1") ? true : false);
        }
    }

    public class PirSinsor implements View.OnClickListener {
        ImageView ButON,
                ButOff,
                ButOnAndOff,
                ButOffAndOn,
                ButSend;
        TextView TextState,
                TextS;
        CircularSeekBar SeekS;
        int StateSinsor = 2;

        public PirSinsor() {
            ButON = linearLayout.findViewById(R.id.but_pir_on);
            ButOff = linearLayout.findViewById(R.id.but_pir_off);
            ButOnAndOff = linearLayout.findViewById(R.id.but_pir_on_off);
            ButOffAndOn = linearLayout.findViewById(R.id.but_pir_off_on);
            ButSend = linearLayout.findViewById(R.id.but_sinsor_send_data);

            TextState = linearLayout.findViewById(R.id.text_sinsor_state);
            TextS = linearLayout.findViewById(R.id.text_sinsor_s);

            SeekS = linearLayout.findViewById(R.id.seek_sinsor_pir);

            SeekS.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
                @Override
                public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                    if (progress < 5) {
                        progress = 5;
                        SeekS.setProgress(progress);
                    }
                    TextS.setText(String.valueOf(progress) + " S");

                }

                @Override
                public void onStopTrackingTouch(CircularSeekBar seekBar) {

                }

                @Override
                public void onStartTrackingTouch(CircularSeekBar seekBar) {

                }
            });
            ButON.setOnClickListener(this);
            ButOff.setOnClickListener(this);
            ButOnAndOff.setOnClickListener(this);
            ButOffAndOn.setOnClickListener(this);
            ButSend.setOnClickListener(this);
            this.setInfoPir();
        }

        @Override
        public void onClick(View view) {//send
            ImageView but = (ImageView) view;
            if (but.getTag().toString().contentEquals("send"))
                ServiceConnection.sendSensorPirMessage("option_pir", SeekS.getProgress(), String.valueOf(StateSinsor), this.getLedPir(), "send");
            else
                this.setState(but.getTag().toString());
        }

        void setState(String state) {
            switch (state) {
                case "2"://on
                    TextState.setText("state:on");
                    StateSinsor = 2;
                    return;

                case "3"://off
                    TextState.setText("state:off");
                    StateSinsor = 3;
                    return;

                case "1"://on_off
                    TextState.setText("state:on->off");
                    StateSinsor = 1;
                    return;

                case "4"://off_on
                    TextState.setText("state:off->on");
                    StateSinsor = 4;
                    return;
            }
        }

        String getLedPir() {
            ArrayList<String> led = new ArrayList<>();
            try {

                for (int index = 0; index < ServiceConnection.Led.length(); index++) {
                    led.add(ServiceConnection.Led.getJSONObject(index).getString("pir"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return led.toString();
        }

        public void setInfoPir() {
            try {
                this.setState(ServiceConnection.Sensor_State.getString("mode"));
                this.SeekS.setProgress(ServiceConnection.Sensor_State.getInt("time"));
                ObjectAnimator.ofFloat(this.ButSend,"Rotation",0f,360f).setDuration(300).start();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public View getViewByPosition(int pos) {
        final int firstListItemPosition = listViewLid.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listViewLid.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listViewLid.getAdapter().getView(pos, null, listViewLid);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listViewLid.getChildAt(childIndex);
        }
    }

    public JSONArray removeItemJson(JSONArray jsonArray, int position) {

        ArrayList<String> list = new ArrayList<>();
        int len = jsonArray.length();
        if (jsonArray != null) {
            for (int i = 0; i < len; i++) {

                try {
                    list.add(jsonArray.get(i).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        //Remove the element from arraylist
        list.remove(position);
        //Recreate JSON Array
        try {
            jsonArray = new JSONArray(list.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public void setEditeDashInfo(String name , String state){
        switch (name){

            case "User":
                if(state=="+") {
                    int newtext=Integer.parseInt(text_user_dahs.getText().toString())+1;
                    prog_user_dash.setProgress(newtext * 5);
                    text_user_dahs.setText(newtext+"");
                } else {
                    int newtext=Integer.parseInt(text_user_dahs.getText().toString())-1;
                    prog_user_dash.setProgress(newtext * 5);
                    text_user_dahs.setText(newtext+"");
                }
                break;

                case "User Wait":
                if(state=="+") {
                    int newtext=Integer.parseInt(text_user_wait_dash.getText().toString())+1;
                    prog_user_wait_dash.setProgress(newtext * 5);
                    text_user_wait_dash.setText(newtext+"");
                } else {
                    int newtext=Integer.parseInt(text_user_wait_dash.getText().toString())-1;
                    prog_user_wait_dash.setProgress(newtext * 5);
                    text_user_wait_dash.setText(newtext+"");
                }
                break;

                case "User Active":
                if(state=="+") {
                    int newtext=Integer.parseInt(text_user_active_dash.getText().toString())+1;
                    prog_user_active_dash.setProgress(newtext * 5);
                    text_user_active_dash.setText(newtext+"");
                } else {
                    int newtext=Integer.parseInt(text_user_active_dash.getText().toString())-1;
                    prog_user_active_dash.setProgress(newtext * 5);
                    text_user_active_dash.setText(newtext+"");
                }
                break;

                case "connection":
                    int newtext=Integer.parseInt(text_connection_dash.getText().toString())+1;
                    prog_connection_dash.setProgress(newtext);
                    text_connection_dash.setText(newtext+"");

                break;

        }
    }
}
