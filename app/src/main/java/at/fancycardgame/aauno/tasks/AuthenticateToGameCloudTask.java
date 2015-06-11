package at.fancycardgame.aauno.tasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Toast;

import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.session.SessionService;
import com.shephertz.app42.paas.sdk.android.user.User;
import com.shephertz.app42.paas.sdk.android.user.UserService;

import at.fancycardgame.aauno.GameActivity;
import at.fancycardgame.aauno.LoginDialogFragment;
import at.fancycardgame.aauno.MainActivity;
import at.fancycardgame.aauno.R;
import at.fancycardgame.aauno.toolbox.Tools;

/**
 * Created by Thomas on 30.05.2015.
 */
public class AuthenticateToGameCloudTask extends AsyncTask<Void, Void, Void> {
    public ConnectivityManager conM;

    private String username;
    private String pwd;

    public static boolean isConnectedToCloud = false;

    public AuthenticateToGameCloudTask(String username, String pwd) {


        this.conM = (ConnectivityManager)Tools.mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        this.username = username;
        this.pwd = pwd;
    }

    @Override
    protected void onPreExecute() {
        Tools.mainActivity.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
    }


    public ConnectivityManager getConnection(){
        return conM;
    }

    @Override
    protected Void doInBackground(Void... params) {

        UserService userService = App42API.buildUserService();




        // 1) authenticate



        userService.authenticate(this.username, this.pwd, new App42CallBack() {
            @Override
            public void onSuccess(Object response) {
                User user = (User) response;

                at.fancycardgame.aauno.User.setUsername(user.getUserName());
                at.fancycardgame.aauno.User.setPwd(user.getPassword());
                at.fancycardgame.aauno.User.setEmail(user.getEmail());
                at.fancycardgame.aauno.User.login();


                // 2) connect to cloud
                Tools.wClient.connectWithUserName(at.fancycardgame.aauno.User.getUsername());




                // not needed show Toast only when connected to the cloud
                //Tools.showToast("User successfully logged in.", Toast.LENGTH_SHORT);
            }



            @Override
            public void onException(Exception ex) {                                                 //TODO: add ex. msg or not?
                Tools.showToast("Error authenticating to game cloud! Right username? Right password?"/* + ex.getMessage()*/, Toast.LENGTH_LONG);

                DialogFragment loginDialog = new LoginDialogFragment();
                loginDialog.show(Tools.mainActivity.getSupportFragmentManager(), "login");

                Tools.mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Tools.mainActivity.findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
        return null;
    }




    @Override
    protected void onPostExecute(Void result) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                while(true) {
                    // static variable will be set to true in ConnectionRequestListener --> onConnectDone callback
                    if(AuthenticateToGameCloudTask.isConnectedToCloud) {
                        Tools.mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Tools.mainActivity.findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);

                                // start new gameActivity
                               // Tools.mainActivity.startActivity(new Intent(Tools.mainActivity, MainActivity.class));

                                // "cleanup"
                                //Tools.mainActivity = null;
                                //Tools.mainActivity.isFinishing();
                            }
                        });
                        AuthenticateToGameCloudTask.isConnectedToCloud = false;
                        break;
                    }
                }
            }
        };
        new Thread(r).start();
    }




}
