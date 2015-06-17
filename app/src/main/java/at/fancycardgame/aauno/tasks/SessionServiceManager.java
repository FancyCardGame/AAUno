package at.fancycardgame.aauno.tasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.tv.TvInputService;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Toast;

import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.session.Session;
import com.shephertz.app42.paas.sdk.android.session.SessionService;
import com.shephertz.app42.paas.sdk.android.user.User;
import com.shephertz.app42.paas.sdk.android.user.UserService;

import at.fancycardgame.aauno.GameActivity;
import at.fancycardgame.aauno.LoginDialogFragment;
import at.fancycardgame.aauno.MainActivity;
import at.fancycardgame.aauno.R;
import at.fancycardgame.aauno.toolbox.Tools;

/**
 * Created by Christian on 10.06.2015.
 */
public class SessionServiceManager extends AsyncTask<Void, Void, Void> {

    private ConnectivityManager conM;
    boolean  isCreate = true;

    String username; //= at.fancycardgame.aauno.User.getUsername();

    public SessionServiceManager(String username) {
        this.conM = (ConnectivityManager) Tools.mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        this.username = username;
    }

    protected Void doInBackground(Void... params) {
        SessionService sessionService = App42API.buildSessionManager();
        sessionService.getSession(username,isCreate, new App42CallBack() {
            @Override
            public void onSuccess(Object response) {
              Session session = (Session)response;
              Tools.sessionID = session.getSessionId();
            }
            @Override
            public void onException(Exception e) {      }
        });
        return null;
    }
}


