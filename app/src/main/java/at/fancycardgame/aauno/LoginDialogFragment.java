package at.fancycardgame.aauno;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Diete on 12.05.2015.
 */
public class LoginDialogFragment extends DialogFragment {



    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        /*final ArrayList currentUser;
        int checkedItem=-1;
        currentUser = new ArrayList();#*/

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_login, null))
                /*.setSingleChoiceItems(R.string.remember, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // If the user checked the item, add it to the selected items
                       currentUser.add(which);
                    }
                })*/
                .setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        MainActivity.login(((TextView)getDialog().findViewById(R.id.username_dialog)).getText().toString(),((TextView)getDialog().findViewById(R.id.password_dialog)).getText().toString());

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
