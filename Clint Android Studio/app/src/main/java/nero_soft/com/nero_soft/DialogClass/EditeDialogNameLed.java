package nero_soft.com.nero_soft.DialogClass;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;


import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;


import nero_soft.com.nero_soft.MainActivity;
import nero_soft.com.nero_soft.R;
import nero_soft.com.nero_soft.ServiceConnection;
import nero_soft.com.nero_soft.interFace.Setting;


/**
 * Created by nero on 05/02/18.
 */

public class EditeDialogNameLed extends DialogFragment {
    View form;
    ListView listViewLid;

    MySimpleArrayAdapter MAD;
    private LayoutInflater Inflater;
    private ViewGroup Container;

    public EditeDialogNameLed() {

    }

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
            super(context, R.layout.lay_list_dash, item_dash);
            Inflater = inflater;
            this.ItemDash = item_dash;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ChangeNameLed changeNameLed = new ChangeNameLed(convertView, Inflater, parent, this.ItemDash.get(position), position);

            return changeNameLed.getViewNameLed();

        }
    }

    class ChangeNameLed implements View.OnClickListener {
        EditText EdittNAmeLed;
        ImageView ImgLed;
        Button ButUpdate;
       public ItemDash ItemDash;
        View ViewNameLed;
        int Position;

        public ChangeNameLed(View convertView, LayoutInflater inflater, ViewGroup parent, ItemDash itemDash, int position) {
            this.ViewNameLed = convertView == null ? inflater.inflate(R.layout.lay_row_change_name_led, parent, false) : convertView;
            this.ItemDash = itemDash;
            this.Position = position;
            this.setUp();
        }

        public void setUp() {
            this.ImgLed = this.ViewNameLed.findViewById(R.id.img_list_dash);
            this.EdittNAmeLed = this.ViewNameLed.findViewById(R.id.edit_list_dash);
            this.ButUpdate = this.ViewNameLed.findViewById(R.id.but_list_dash);


            this.EdittNAmeLed.setText(this.ItemDash.Name);
            this.ButUpdate.setOnClickListener(this);
        }


        public View getViewNameLed() {
            return this.ViewNameLed;
        }

        @Override
        public void onClick(View v) {

            ServiceConnection.MeassagDialog("Change Name " + ItemDash.Name +
                    " TO { " + this.EdittNAmeLed.getText().toString() + " }", this.ViewNameLed, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        ServiceConnection.setChangeLedName(new Setting.GetPost.getSettingLedInfo() {
                            @Override
                            public void PostExecuteSettingLedInfo(JSONObject result) {
                                try {
                                    if(result.getBoolean("state")){
                                        Snackbar.make( ViewNameLed,"You Have a New Successful",Snackbar.LENGTH_LONG).show();
                                      ServiceConnection.Led.getJSONObject(Position).put("name",EdittNAmeLed.getText().toString());
                                      MainActivity.SmartHome.smartHome.notifyItemChanged(Position);
                                        MAD.ItemDash.get(Position).Name=EdittNAmeLed.getText().toString();
                                    }else    Snackbar.make( ViewNameLed,"Errore",Snackbar.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new JSONObject().put("id",ItemDash.Id).put("newname",EdittNAmeLed.getText().toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

    }

    public class ItemDash {
       int Id;
        String Name;

        public ItemDash(String name ,int id) {
            this.Name = name;
            this.Id = id;

        }
    }

    public ArrayList jsonToArray() {
        ArrayList<ItemDash> array = new ArrayList();
        try {
            for (int i = 0; i < ServiceConnection.Led.length(); i++)
                array.add(new ItemDash(ServiceConnection.Led.getJSONObject(i).getString("name"),
                        ServiceConnection.Led.getJSONObject(i).getInt("id")));

            return array;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }

}