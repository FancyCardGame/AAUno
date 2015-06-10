package at.fancycardgame.aauno;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import at.fancycardgame.aauno.listeners.AbstractAnimationListener;

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
                    startActivity(new Intent(Usermanager.this, CreateUserActivity.class));
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


        }

    }

    private void setOptionsMenuTypeface() {
        Typeface menu_userm = Typeface.createFromAsset(getAssets(), menu_font);
        setStringTypeface(R.id.createUserMP);
        setStringTypeface(R.id.changePwdMP);


    }

    private void setStringTypeface(int textview) {
        Typeface options_menu = Typeface.createFromAsset(getAssets(), menu_font);
        ((TextView) findViewById(textview)).setTypeface(options_menu);
    }
}