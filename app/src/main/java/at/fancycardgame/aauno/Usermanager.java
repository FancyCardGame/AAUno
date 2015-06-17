package at.fancycardgame.aauno;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.App42Response;
import com.shephertz.app42.paas.sdk.android.user.UserService;

import at.fancycardgame.aauno.listeners.AbstractAnimationListener;
import at.fancycardgame.aauno.toolbox.Tools;

/**
 * Created by Christian on 26.05.2015.
 */
public class Usermanager extends Activity implements View.OnClickListener {

    private View.OnClickListener mainOnClickListener = this;
    private static final String menu_font = "Comic Book Bold.ttf";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.menu_usermgmt_page);

        setOptionsMenuTypeface();
        findViewById(R.id.createUserMP).setOnClickListener(mainOnClickListener);
        findViewById(R.id.changePwdMP).setOnClickListener(mainOnClickListener);
        if(!User.isLoggedIn()){
        View logout = findViewById(R.id.logoutMP);
        logout.setVisibility(View.INVISIBLE);
        }
        else
            findViewById(R.id.logoutMP).setOnClickListener(mainOnClickListener);
    }


    public void onClick(View clickedView) {
        //OnClickListener that determines which TextView has been clicked by ID
        int clickedID = clickedView.getId();

        // create pulse animation when clicking on menue textviews
        Animation a = AnimationUtils.loadAnimation(this, R.anim.pulse);

        if (clickedID == R.id.createUserMP) {
                a.setAnimationListener(new AbstractAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // access user mgmt
                        if (!User.isLoggedIn()) {
                            startActivity(new Intent(Usermanager.this, CreateUserActivity.class));
                        }else{
                            Toast toast = Toast.makeText(getApplicationContext(), "You have to logout to create a new user", Toast.LENGTH_SHORT );
                            toast.show();
                        }
                    }
                });
                clickedView.startAnimation(a);
        } else if (clickedID == R.id.changePwdMP) {
            a.setAnimationListener(new AbstractAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    //access change password
                    //startActivity(new Intent(Usermanager.this, ChangePwActivity.class));
                }
            });
            clickedView.startAnimation(a);
        }else if(clickedID == R.id.logoutMP){
            a.setAnimationListener(new AbstractAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    findViewById(R.id.logoutMP).setVisibility(View.INVISIBLE);
                    User.logout();
                    UserService userService = App42API.buildUserService();
                    userService.logout(Tools.sessionID, new App42CallBack() {
                        public void onSuccess(Object response) {
                            App42Response app42response = (App42Response) response;
                        }
                        public void onException(Exception e) {

                        }
                    });
                }});
            clickedView.startAnimation(a);
            //startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }
    private void setOptionsMenuTypeface() {
        Tools.setStringTypeface(this, R.id.createUserMP);
        Tools.setStringTypeface(this, R.id.changePwdMP);
        Tools.setStringTypeface(this, R.id.logoutMP);
    }
}