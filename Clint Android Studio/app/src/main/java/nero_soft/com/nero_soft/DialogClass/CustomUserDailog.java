package nero_soft.com.nero_soft.DialogClass;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import nero_soft.com.nero_soft.MainActivity;
import nero_soft.com.nero_soft.R;
import nero_soft.com.nero_soft.ServiceConnection;
import nero_soft.com.nero_soft.ViewHolder.DashBoard;
import nero_soft.com.nero_soft.animation.MyAinm;
import nero_soft.com.nero_soft.interFace.Setting;

/**
 * Created by nero on 05/02/18.
 */

public class CustomUserDailog extends DialogFragment {
    View form;
    ListView listViewLid;

    MySimpleArrayAdapter MAD;
    private LayoutInflater Inflater;
    private ViewGroup Container;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        form = inflater.inflate(R.layout.lay_change_name_led, container);
        this.Inflater = inflater;
        this.Container = container;
        listViewLid = form.findViewById(R.id.list_name_led);
        MAD = new MySimpleArrayAdapter(getActivity().getApplicationContext(), this.Inflater, this.jsonToArray());
        listViewLid.setAdapter(MAD);
        return form;
    }


    public class MySimpleArrayAdapter extends ArrayAdapter {
        ArrayList<ItemDash> ItemDash;
        LayoutInflater Inflater;

        public MySimpleArrayAdapter(Context context, LayoutInflater inflater, ArrayList<ItemDash> item_dash) {
            super(context, R.layout.lay_row_custom_user, item_dash);
            Inflater = inflater;
            this.ItemDash = item_dash;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            CustomUser customUser = new CustomUser(convertView, Inflater, parent, this.ItemDash.get(position), position);

            return customUser.getViewNameLed();

        }
    }

    class CustomUser implements View.OnClickListener {
        TextView TextNAmeUser;
        ImageView ImgLed;
        FloatingActionButton ButEdit, ButDelete, ButActive;
        public ItemDash ItemDash;
        View ViewCustomUser;
        int Position;

        public CustomUser(View convertView, LayoutInflater inflater, ViewGroup parent, ItemDash itemDash, int position) {
            this.ViewCustomUser = inflater.inflate(R.layout.lay_row_custom_user, parent, false);
            this.ItemDash = itemDash;
            this.Position = position;
            this.setUp();
        }

        public void setUp() {
            this.ImgLed = this.ViewCustomUser.findViewById(R.id.img_list_custom_user);
            this.TextNAmeUser = this.ViewCustomUser.findViewById(R.id.text_list_custom_user);

            this.ButEdit = this.ViewCustomUser.findViewById(R.id.but_list_edit_user);
            this.ButDelete = this.ViewCustomUser.findViewById(R.id.but_list_delete_user);
            this.ButActive = this.ViewCustomUser.findViewById(R.id.but_list_active_user);


            this.TextNAmeUser.setText(this.ItemDash.Email);
            this.ButEdit.setOnClickListener(this);
            this.ButDelete.setOnClickListener(this);
            this.ButActive.setOnClickListener(this);
            if (ItemDash.GroupU == 1)
                this.ButActive.setVisibility(View.GONE);
        }


        public View getViewNameLed() {
            return this.ViewCustomUser;
        }

        @Override
        public void onClick(View v) {
            FloatingActionButton button = (FloatingActionButton) v;
            switch (button.getTag().toString()) {

                case "edite":
                   this.editeUser();
                    break;

                case "delete":
                    this.deleteUser();
                    break;

                case "active":
                    this.activeUser();

                    break;
            }



        }

        private void editeUser() {
            new EditeDialogUser().EditUser("EDITE",new DashBoard.ItemDash(ItemDash.Id,ItemDash.Email,ItemDash.GroupU,ItemDash.Comment,"EDITE"), Position).show(getActivity().getFragmentManager(), "");
            getDialog().cancel();
        }

        public void activeUser() {
            ServiceConnection.MeassagDialog("Do You Want Active This User", ViewCustomUser, new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    final JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("id", ItemDash.Id);
                        jsonObject.put("optin", "active");
                        ServiceConnection.setEditeMemberInfo(new Setting.GetPost.getSettingLedInfo() {
                            @Override
                            public void PostExecuteSettingLedInfo(JSONObject result) {
                                try {
                                    if (result.getBoolean("state")) {
                                        ServiceConnection.Dash_Info.getJSONArray("users")
                                                .getJSONObject(Position).put("GroupU", "1");
                                        MAD.ItemDash.get(Position).GroupU = 1;
                                        MyAinm.fade(new MyAinm.ClickAction() {
                                            @Override
                                            public void click() {
                                                if (MainActivity.DashBoard.dashBoard.MAD.Position == 3) {
                                                    for (int i = 0; i < MainActivity.DashBoard.dashBoard.array.size(); i++)
                                                        if (MainActivity.DashBoard.dashBoard.array.get(i).Id == ItemDash.Id){
                                                            MainActivity.DashBoard.dashBoard.array.remove(i);
                                                            break;
                                                    }
                                                    MainActivity.DashBoard.dashBoard.MAD.notifyDataSetChanged();
                                                }else if(MainActivity.DashBoard.dashBoard.MAD.Position == 2){
                                                       MainActivity.DashBoard.dashBoard.array.get(Position).GroupU=1;
                                                    MainActivity.DashBoard.dashBoard.MAD.notifyDataSetChanged();
                                                }
                                                MAD.notifyDataSetChanged();
                                                MainActivity.DashBoard.dashBoard.setEditeDashInfo("User Wait","-");
                                            }
                                        }, ButActive, 500, 1.0f, 0.0f);

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

        public void deleteUser() {
            ServiceConnection.MeassagDialog("Do You Want Delete This User", ViewCustomUser, new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    final JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("id", ItemDash.Id);
                        jsonObject.put("optin", "delete");
                        ServiceConnection.setEditeMemberInfo(new Setting.GetPost.getSettingLedInfo() {
                            @Override
                            public void PostExecuteSettingLedInfo(JSONObject result) {
                                try {
                                    if (result.getBoolean("state")) {
                                        ServiceConnection.Dash_Info.put("users",removeItemJson(ServiceConnection.Dash_Info.getJSONArray("users"),Position));
                                        MyAinm.fade(new MyAinm.ClickAction() {
                                            @Override
                                            public void click() {
                                                if (MainActivity.DashBoard.dashBoard.MAD.Position == 2) {

                                                    MainActivity.DashBoard.dashBoard.array.remove(Position);
                                                    MainActivity.DashBoard.dashBoard.MAD.notifyDataSetChanged();

                                                }else if(ItemDash.GroupU==0&&MainActivity.DashBoard.dashBoard.MAD.Position == 3){
                                                    for (int i = 0; i < MainActivity.DashBoard.dashBoard.array.size(); i++)
                                                        if (MainActivity.DashBoard.dashBoard.array.get(i).Id == ItemDash.Id){
                                                            MainActivity.DashBoard.dashBoard.array.remove(i);
                                                            break;
                                                        }
                                                    MainActivity.DashBoard.dashBoard.MAD.notifyDataSetChanged();
                                                }
                                                MAD.ItemDash.remove(Position);
                                                MAD.notifyDataSetChanged();
                                                MainActivity.DashBoard.dashBoard.setEditeDashInfo("User","-");
                                            }
                                        }, ViewCustomUser, 500, 1.0f, 0.0f);

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

    public class ItemDash {
        int Id;
        String Email;
        String Password;
        int GroupU;
        String Comment;

        public ItemDash(int id, String email, int GroupU, String comment) {
            this.Id = id;
            this.Email = email;
            this.Password = "-------";
            this.GroupU = GroupU;
            this.Comment = comment;

        }
    }

    public ArrayList jsonToArray() {
        ArrayList<ItemDash> array = new ArrayList();
        try {
            JSONArray User = ServiceConnection.Dash_Info.getJSONArray("users");

            for (int i = 0; i < User.length(); i++)
                array.add(new ItemDash(User.getJSONObject(i).getInt("id"),
                        User.getJSONObject(i).getString("email"),
                        User.getJSONObject(i).getInt("GroupU"),
                        User.getJSONObject(i).getString("comment")));
            return array;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
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
}