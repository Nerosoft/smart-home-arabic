package nero_soft.com.nero_soft.DialogClass;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import nero_soft.com.nero_soft.R;


/**
 * Created by nero on 06/02/18.
 */

public class DailogMessage extends DialogFragment {
    Button buttonC;
    Button buttonB;
    View.OnClickListener ClickListener;
    String Titl;
    String Message;


    public DailogMessage setDialogMessage(String TITLE, String MESSAGE,View.OnClickListener clickListener) {
        this.Titl = TITLE;
        this.Message = MESSAGE;
        this.ClickListener=clickListener;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(Message)
                .setTitle(Titl).setIcon(R.drawable.ic_dashboard)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });



        AlertDialog dialog = builder.create();
        dialog.show();

        buttonB = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonB.setOnClickListener(this.ClickListener);

        buttonC = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        getDialog().cancel();
            }
        });
        //check box even checked

        return dialog;
        // Create the AlertDialog object and return it

    }

}//upclass
