package at.fancycardgame.aauno;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import at.fancycardgame.aauno.listeners.AbstractAnimationListener;
import at.fancycardgame.aauno.toolbox.Tools;

/**
 * Created by Christian on 26.05.2015.
 */
public class OptionsActivity extends Activity implements View.OnClickListener  {

   // public ViewGroup userMgmtMenu;
    public ViewGroup createUserMenu;

    private View.OnClickListener mainOnClickListener = this;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.menu_options_page);

        this.createUserMenu = (ViewGroup)getLayoutInflater().inflate(R.layout.menu_createuser_page, null);

        findViewById(R.id.userMgmtMP).setOnClickListener(mainOnClickListener);
        findViewById(R.id.musicOnOffMP).setOnClickListener(mainOnClickListener);
        findViewById(R.id.effectsOnOffMP).setOnClickListener(mainOnClickListener);

        setOptionsMenuTypeface();
    }

    public void onClick(View clickedView) {
        //OnClickListener that determines which TextView has been clicked by ID
        int clickedID = clickedView.getId();

        // create pulse animation when clicking on menue textviews
        Animation a = AnimationUtils.loadAnimation(this, R.anim.pulse);

        if (clickedID == R.id.userMgmtMP) {
            a.setAnimationListener(new AbstractAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    // access user mgmt
                    startActivity(new Intent(OptionsActivity.this,Usermanager.class));
                }
            });
            clickedView.startAnimation(a);
        }
    }
    private void setOptionsMenuTypeface() {
        Tools.setStringTypeface(this, R.id.userMgmtMP);
        Tools.setStringTypeface(this, R.id.musicOnOffMP);
        Tools.setStringTypeface(this, R.id.effectsOnOffMP);
    }
}