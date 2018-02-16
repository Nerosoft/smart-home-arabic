package nero_soft.com.nero_soft.DialogClass;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONException;
import org.json.JSONObject;
import nero_soft.com.nero_soft.R;
import nero_soft.com.nero_soft.ServiceConnection;
import nero_soft.com.nero_soft.interFace.Setting;
import nero_soft.com.nero_soft.networkSocket.UserLoginTask;

/**
 * Created by nero on 06/02/18.
 */

public class ChangeAdmin extends DialogFragment implements View.OnClickListener {

    EditText edit_oldpassword ,edit_newpassword ;
    Button buttonC;
    Button buttonO;
    View ViewDailog;
    AlertDialog.Builder builder;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        ViewDailog = inflater.inflate(R.layout.lay_change_admin, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(ViewDailog)
                // Add action buttons
                .setPositiveButton("Change !", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...


                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Dialog_Edit.this.getDialog().cancel();
                    }
                });
        this.edit_oldpassword=this.ViewDailog.findViewById(R.id.edit_oldpassword);
        this.edit_newpassword=this.ViewDailog.findViewById(R.id.edit_newpassword);

        AlertDialog dialog = builder.create();
        dialog.show();

        buttonO = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonO.setOnClickListener(this);

        buttonC = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             getDialog().cancel();
            }
        });

        return dialog;
    }





    public boolean chekInfo(){
        boolean ok=true;

        if (edit_oldpassword.getText().toString().length()<7) {
            ok = false;
            edit_oldpassword.setError("pass");
            edit_oldpassword.setError("pass");
        }
        if (edit_newpassword.getText().toString().length()<7) {
            ok = false;
            edit_newpassword.setError("pass");
            edit_newpassword.setError("pass");
        }

        return ok;
    }


    @Override
    public void onClick(View view) {
        if(chekInfo()){
            if (mAuthTask != null) {
                return;
            }
            try {
                    setChangeAdmin(new Setting.GetPost.getSettingLedInfo() {
                    @Override
                    public void PostExecuteSettingLedInfo(JSONObject result) {
                        try {
                            if (result.getBoolean("state")){

                                getDialog().cancel();

                            }else {
                                edit_oldpassword.setError("Error password");
                                edit_newpassword.setError("Error password");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new JSONObject().put("password",edit_oldpassword.getText().toString())
                        .put("newpassword",edit_newpassword.getText().toString())
                        .put("email",ServiceConnection.User));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private UserLoginTask mAuthTask = null;
    public void setChangeAdmin(final Setting.GetPost.getSettingLedInfo getSettingLedInfo, JSONObject Send_Data) {

        mAuthTask = new UserLoginTask("cangeadmin", Send_Data, new Setting.SetPost() {
            @Override
            public void PostExecute(JSONObject result) {
                mAuthTask=null;
                getSettingLedInfo.PostExecuteSettingLedInfo(result);

            }

            @Override
            public void Cansle() {
                mAuthTask=null;
            }
        });
        mAuthTask.execute((Void) null);
    }

}