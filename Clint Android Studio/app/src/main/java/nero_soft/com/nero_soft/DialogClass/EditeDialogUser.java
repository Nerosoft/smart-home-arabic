package nero_soft.com.nero_soft.DialogClass;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nero_soft.com.nero_soft.MainActivity;
import nero_soft.com.nero_soft.R;
import nero_soft.com.nero_soft.ServiceConnection;
import nero_soft.com.nero_soft.ViewHolder.DashBoard;
import nero_soft.com.nero_soft.interFace.Setting;

/**
 * Created by nero on 01/02/18.
 */

public class EditeDialogUser extends DialogFragment {

    EditText edit_user, edit_password, edit_confirm_password, edit_comment;
    Button buttonC;
    Button buttonO;
    Spinner spinner;
    int Position;
    String NamePositiveButton;
    View ViewDailog;
    View.OnClickListener ClickListener;
    AlertDialog.Builder builder;
    DashBoard.ItemDash itemDash;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        ViewDailog = inflater.inflate(R.layout.lay_edit_user, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(ViewDailog)
                // Add action buttons
                .setPositiveButton(this.NamePositiveButton, new DialogInterface.OnClickListener() {
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
        this.edit_user = this.ViewDailog.findViewById(R.id.edit_user);
        this.edit_password = this.ViewDailog.findViewById(R.id.edit_password);
        this.edit_confirm_password = this.ViewDailog.findViewById(R.id.edit_confirm_password);
        this.edit_comment = this.ViewDailog.findViewById(R.id.edit_comment);
        this.setSpinner(ViewDailog);
        AlertDialog dialog = builder.create();
        dialog.show();
        if (this.NamePositiveButton == "EDITE") SetupDialogEditUser();
        else SetupDialogCreateUser();
        buttonO = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonO.setOnClickListener(this.ClickListener);

        buttonC = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditeDialogUser.this.getDialog().cancel();
            }
        });
        return dialog;
    }

    public EditeDialogUser EditUser(String name_positive_button, DashBoard.ItemDash itemDash, int position) {
        this.NamePositiveButton = name_positive_button;
        this.itemDash = itemDash;
        this.Position = position;
        return this;
    }

    public EditeDialogUser CreateUser(String name_positive_button) {
        this.NamePositiveButton = name_positive_button;
        return this;
    }

    public void SetupDialogEditUser() {
        this.edit_user.setText(itemDash.Name);
        this.edit_comment.setText(itemDash.comment);
        this.spinner.setSelection(itemDash.GroupU);
        this.edit_password.setText("----------");
        this.edit_confirm_password.setText("----------");

        this.ClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chekInfo()) {
                    final JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("id", itemDash.Id);
                        jsonObject.put("user", edit_user.getText().toString());
                        jsonObject.put("email", edit_user.getText().toString());
                        jsonObject.put("password", edit_password.getText().toString());
                        jsonObject.put("GroupU", spinner.getSelectedItemPosition());
                        jsonObject.put("comment", edit_comment.getText().toString());
                        ServiceConnection.setEditeMemberInfo(new Setting.GetPost.getSettingLedInfo() {
                            @Override
                            public void PostExecuteSettingLedInfo(JSONObject result) {
                                try {
                                    if (result.getBoolean("state")) {
                                        ServiceConnection.Dash_Info.getJSONArray("users").put(Position, jsonObject);
                                        if (itemDash.GroupU != spinner.getSelectedItemPosition())
                                                MainActivity.DashBoard.dashBoard.setEditeDashInfo("User Wait",
                                                        spinner.getSelectedItemPosition()==1?"-":"+");

                                        MainActivity.DashBoard.dashBoard.array.get(Position).Name = jsonObject.getString("user");
                                        MainActivity.DashBoard.dashBoard.array.get(Position).GroupU = jsonObject.getInt("GroupU");
                                        MainActivity.DashBoard.dashBoard.array.get(Position).comment = jsonObject.getString("comment");
                                        MainActivity.DashBoard.dashBoard.MAD.notifyDataSetChanged();

                                        EditeDialogUser.this.getDialog().cancel();
                                    } else edit_user.setError("Name");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new JSONObject().put("user", jsonObject).put("optin", "update"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    public void SetupDialogCreateUser() {


        this.ClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chekInfo()) {
                    final JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("user", edit_user.getText().toString());
                        jsonObject.put("email", edit_user.getText().toString());
                        jsonObject.put("password", edit_password.getText().toString());
                        jsonObject.put("groupid", spinner.getSelectedItemPosition());
                        jsonObject.put("GroupU", spinner.getSelectedItemPosition());
                        jsonObject.put("comment", edit_comment.getText().toString());
                        ServiceConnection.setCreateMemberInfo(new Setting.GetPost.getSettingLedInfo() {
                            @Override
                            public void PostExecuteSettingLedInfo(JSONObject result) {
                                try {
                                    if (result.getBoolean("state")) {
                                        ServiceConnection.Dash_Info.getJSONArray("users").put(jsonObject.put("id",
                                                result.getJSONObject("newuser").getInt("id")));
                                        if (MainActivity.DashBoard.dashBoard.MAD.Position == 2) {
                                            MainActivity.DashBoard.dashBoard.array.get(Position).Name = jsonObject.getString("user");
                                            MainActivity.DashBoard.dashBoard.array.get(Position).Id = jsonObject.getInt("id");
                                            MainActivity.DashBoard.dashBoard.array.get(Position).GroupU = jsonObject.getInt("GroupU");
                                            MainActivity.DashBoard.dashBoard.array.get(Position).comment = jsonObject.getString("comment");
                                            MainActivity.DashBoard.dashBoard.MAD.notifyDataSetChanged();
                                        } else if (jsonObject.getInt("GroupU") == 0 && MainActivity.DashBoard.dashBoard.MAD.Position == 3) {
                                            MainActivity.DashBoard.dashBoard.array.add(new DashBoard.ItemDash(jsonObject.getString("email"),
                                                    jsonObject.getInt("id"), "ACTIVE"));
                                            MainActivity.DashBoard.dashBoard.MAD.notifyDataSetChanged();
                                        }
                                        if (jsonObject.getInt("GroupU") == 0)
                                            MainActivity.DashBoard.dashBoard.setEditeDashInfo("User Wait", "+");
                                        MainActivity.DashBoard.dashBoard.setEditeDashInfo("User", "+");
                                        EditeDialogUser.this.getDialog().cancel();
                                    } else edit_user.setError("Name");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new JSONObject().put("adduser", jsonObject));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    public boolean chekInfo() {
        boolean ok = true;
        if (edit_user.getText().toString().length() < 7) {
            ok = false;
            edit_user.setError("Name");
        }
        if (!edit_password.getText().toString().contentEquals(edit_confirm_password.getText().toString())) {
            ok = false;
            edit_password.setError("Error Password");
            edit_confirm_password.setError("Error Password");
        }
        if (edit_password.getText().toString().length() < 7) {
            ok = false;
            edit_password.setError("Error Password");
            edit_confirm_password.setError("Error Password");
        }
        if (edit_confirm_password.getText().toString().length() < 7) {
            ok = false;
            edit_password.setError("Error Password");
            edit_confirm_password.setError("Error Password");
        }

        return ok;
    }

    void setSpinner(View view) {
        spinner = view.findViewById(R.id.state_of_user);
        List<String> list = new ArrayList();
        list.add("Wait");
        list.add("Active");
        ArrayAdapter dataAdapter = new ArrayAdapter(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }
}