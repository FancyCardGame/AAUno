package at.fancycardgame.aauno;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;


import java.util.ArrayList;

import at.fancycardgame.aauno.tasks.AuthenticateToGameCloudTask;
import at.fancycardgame.aauno.toolbox.Tools;

/**
 * Created by Diete on 12.05.2015.
 */
public class LoginDialogFragment extends DialogFragment {

    public View.OnClickListener mainOnclickListener;


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


                        String username = ((TextView) getDialog().findViewById(R.id.username_dialog)).getText().toString();
                        String pwd = ((TextView) getDialog().findViewById(R.id.password_dialog)).getText().toString();






                        AuthenticateToGameCloudTask auth = new AuthenticateToGameCloudTask(username, pwd);
                        auth.execute();
                        //MainActivity.login(,);




                    }
                })


                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {




                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        // TODO: do nothing? stay on menu?

                    }



                })

                .setNeutralButton("Sign up", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getActivity(),CreateUserActivity.class));
                    }
                });

































        // Create the AlertDialog object and return it
        return builder.create();



    }

}
